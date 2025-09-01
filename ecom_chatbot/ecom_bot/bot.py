import json
import os
from typing import Any, Dict, List

from .intents import classify_intent, Intent
from .handlers import (
    handle_order_status,
    handle_return_policy,
    handle_refund_policy,
    handle_shipping_info,
    handle_product_search,
    handle_faq,
    handle_greeting,
    handle_goodbye,
    handle_escalate,
)


class EcomCustomerServiceBot:
    def __init__(self) -> None:
        self.knowledge_base: Dict[str, Any] = self._load_knowledge_base()
        self.openai_client = self._init_openai_client()
        self.openai_model = os.getenv("OPENAI_MODEL", "gpt-4o-mini")

    def _kb_path(self, filename: str) -> str:
        base_dir = os.path.dirname(__file__)
        return os.path.join(base_dir, "data", filename)

    def _load_json(self, path: str) -> Any:
        try:
            with open(path, "r", encoding="utf-8") as f:
                return json.load(f)
        except FileNotFoundError:
            return None
        except json.JSONDecodeError:
            return None

    def _load_knowledge_base(self) -> Dict[str, Any]:
        products = self._load_json(self._kb_path("products.json")) or []
        faqs = self._load_json(self._kb_path("faqs.json")) or []
        policies = self._load_json(self._kb_path("policies.json")) or {}
        orders_list = self._load_json(self._kb_path("orders.json")) or []
        orders = {str(o.get("id")): o for o in orders_list if isinstance(o, dict) and o.get("id")}
        return {
            "products": products,
            "faqs": faqs,
            "policies": policies,
            "orders": orders,
        }

    def _init_openai_client(self):
        api_key = os.getenv("OPENAI_API_KEY")
        if not api_key:
            return None
        try:
            from openai import OpenAI

            return OpenAI()
        except Exception:
            return None

    def _llm_fallback(self, message: str, history: List[List[str]]) -> str:
        if not self.openai_client:
            return (
                "I'm not fully sure. Could you rephrase or provide more details? "
                "You can also ask about orders, returns, refunds, shipping, or products."
            )
        try:
            system_prompt = (
                "You are an e-commerce customer support assistant. Be concise, helpful, and safe. "
                "Prefer answers based on provided information. If unsure, say so."
            )
            messages = [{"role": "system", "content": system_prompt}]
            for user_msg, bot_msg in history[-6:]:
                messages.append({"role": "user", "content": user_msg})
                if bot_msg:
                    messages.append({"role": "assistant", "content": bot_msg})
            messages.append({"role": "user", "content": message})

            # Use Chat Completions API
            resp = self.openai_client.chat.completions.create(
                model=self.openai_model,
                messages=messages,
                temperature=0.2,
                max_tokens=300,
            )
            return resp.choices[0].message.content.strip()
        except Exception:
            return (
                "I'm not fully sure. You can contact support or ask about orders, returns, or shipping."
            )

    def respond(self, message: str, history: List[List[str]]) -> str:
        intent, confidence, _ = classify_intent(message)
        kb = self.knowledge_base

        if intent == Intent.GREETING:
            return handle_greeting()
        if intent == Intent.GOODBYE:
            return handle_goodbye()
        if intent == Intent.ORDER_STATUS:
            return handle_order_status(message, kb)
        if intent == Intent.RETURN_POLICY:
            return handle_return_policy(kb)
        if intent == Intent.REFUND_POLICY:
            return handle_refund_policy(kb)
        if intent == Intent.SHIPPING_INFO:
            return handle_shipping_info(kb)
        if intent == Intent.PRODUCT_SEARCH:
            return handle_product_search(message, kb)
        if intent == Intent.FAQ:
            return handle_faq(message, kb)
        if intent == Intent.ESCALATE:
            return handle_escalate(kb)

        # Unknown -> LLM fallback
        return self._llm_fallback(message, history)

