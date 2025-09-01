import re
from typing import Dict, List, Any, Tuple


def _extract_order_id(text: str) -> str:
    if not text:
        return ""
    match = re.search(r"(order\s*#?\s*)?(\d{5,})", text.lower())
    if match:
        return match.group(2)
    return ""


def handle_order_status(text: str, kb: Dict[str, Any]) -> str:
    order_id = _extract_order_id(text)
    if not order_id:
        return (
            "To check your order status, please provide your order number (e.g., 10001)."
        )

    orders: Dict[str, Any] = kb.get("orders", {})
    order = orders.get(order_id)
    if not order:
        return (
            f"I could not find order {order_id}. Please verify the number or contact support."
        )

    status = order.get("status", "Processing")
    carrier = order.get("carrier", "")
    tracking = order.get("tracking", "")
    eta = order.get("eta", "")
    lines = [
        f"Order {order_id} status: {status}.",
    ]
    if tracking:
        lines.append(f"Tracking: {tracking}{' via ' + carrier if carrier else ''}.")
    if eta:
        lines.append(f"Estimated delivery: {eta}.")
    return " \n".join(lines)


def handle_return_policy(kb: Dict[str, Any]) -> str:
    policies = kb.get("policies", {})
    returns = policies.get("returns", {})
    summary = returns.get("summary", "You can return items within 30 days of delivery.")
    steps = returns.get("steps", [])
    bullets = "\n".join([f"- {s}" for s in steps]) if steps else ""
    result = summary
    if bullets:
        result += f"\n\nHow to return:\n{bullets}"
    return result


def handle_refund_policy(kb: Dict[str, Any]) -> str:
    policies = kb.get("policies", {})
    refunds = policies.get("refunds", {})
    parts = []
    if refunds.get("summary"):
        parts.append(refunds["summary"])
    if refunds.get("timeline"):
        parts.append(f"Refund timeline: {refunds['timeline']}")
    if refunds.get("method"):
        parts.append(f"Refund method: {refunds['method']}")
    return "\n".join(parts) or "Refunds are processed to the original payment method within 5-10 business days."


def handle_shipping_info(kb: Dict[str, Any]) -> str:
    policies = kb.get("policies", {})
    shipping = policies.get("shipping", {})
    parts = []
    if shipping.get("summary"):
        parts.append(shipping["summary"])
    if shipping.get("domestic"):
        parts.append(f"Domestic: {shipping['domestic']}")
    if shipping.get("international"):
        parts.append(f"International: {shipping['international']}")
    if shipping.get("free_threshold"):
        parts.append(f"Free shipping threshold: {shipping['free_threshold']}")
    return "\n".join(parts) or "Standard shipping is 3-5 business days. Free over $50."


def _search_products(query: str, products: List[Dict[str, Any]], limit: int = 3) -> List[Dict[str, Any]]:
    if not query:
        return []
    t = query.lower()
    scores: List[Tuple[int, Dict[str, Any]]] = []
    for product in products:
        name = product.get("name", "").lower()
        desc = product.get("description", "").lower()
        cat = product.get("category", "").lower()
        score = 0
        if any(k in name for k in t.split()):
            score += 2
        if any(k in desc for k in t.split()):
            score += 1
        if any(k in cat for k in t.split()):
            score += 1
        if score > 0:
            scores.append((score, product))
    scores.sort(key=lambda x: x[0], reverse=True)
    return [p for _, p in scores[:limit]]


def handle_product_search(text: str, kb: Dict[str, Any]) -> str:
    products = kb.get("products", [])
    matches = _search_products(text, products, limit=5)
    if not matches:
        return "I didn't find matching products. Try different keywords or categories."
    lines = ["Here are some options:"]
    for p in matches:
        stock = "In stock" if p.get("in_stock", True) else "Out of stock"
        price = p.get("price")
        price_str = f"${price:.2f}" if isinstance(price, (int, float)) else ""
        lines.append(f"- {p.get('name')} ({price_str}) — {stock}. {p.get('description')}")
    return "\n".join(lines)


def handle_faq(text: str, kb: Dict[str, Any]) -> str:
    faqs: List[Dict[str, Any]] = kb.get("faqs", [])
    t = (text or "").lower()
    best = None
    best_score = 0
    for item in faqs:
        q = item.get("question", "").lower()
        a = item.get("answer", "")
        score = 0
        for token in t.split():
            if token and token in q:
                score += 1
        if score > best_score:
            best_score = score
            best = a
    if best and best_score > 0:
        return best
    return "You can ask about hours, warranty, payment methods, discounts, or support."


def handle_greeting() -> str:
    return "Hi! I'm your shopping assistant. How can I help today?"


def handle_goodbye() -> str:
    return "Thanks for chatting! If you need anything else, I'm here."


def handle_escalate(kb: Dict[str, Any]) -> str:
    support = kb.get("policies", {}).get("support", {})
    email = support.get("email", "support@example.com")
    phone = support.get("phone", "(555) 010-0000")
    hours = support.get("hours", "Mon–Fri 9am–5pm")
    return (
        f"I can connect you with a human agent. You can also reach us at {email} or {phone}.\n"
        f"Hours: {hours}"
    )

