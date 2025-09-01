import re
from enum import Enum
from typing import Dict, Tuple


class Intent(Enum):
    ORDER_STATUS = "order_status"
    RETURN_POLICY = "return_policy"
    REFUND_POLICY = "refund_policy"
    SHIPPING_INFO = "shipping_info"
    PRODUCT_SEARCH = "product_search"
    ESCALATE = "escalate_human"
    GREETING = "greeting"
    GOODBYE = "goodbye"
    FAQ = "faq"
    UNKNOWN = "unknown"


def classify_intent(text: str) -> Tuple[Intent, float, Dict]:
    """Classify the user's text into a coarse intent with a simple confidence.

    Returns a tuple of (intent, confidence, meta).
    """
    t = (text or "").lower().strip()
    if not t:
        return (Intent.UNKNOWN, 0.0, {})

    score_map: Dict[Intent, float] = {}

    def add_score(intent: Intent, amount: float) -> None:
        score_map[intent] = score_map.get(intent, 0.0) + amount

    # Order status / tracking
    if re.search(r"\border\b|order\s*#?\s*\d+|track(ing)?\b|status|where.*order|package", t):
        add_score(Intent.ORDER_STATUS, 1.0)

    # Returns
    if re.search(r"\breturn(s|ing)?\b|return policy|exchange|how to return|return window", t):
        add_score(Intent.RETURN_POLICY, 1.0)

    # Refunds / cancellations
    if re.search(r"\brefund(s|ed)?\b|money back|cancel.*order", t):
        add_score(Intent.REFUND_POLICY, 1.0)

    # Shipping / delivery
    if re.search(r"ship(ping)?|delivery|deliver|when.*arrive|how long.*ship|international", t):
        add_score(Intent.SHIPPING_INFO, 1.0)

    # Product search
    if re.search(r"\bfind\b|do you have|looking for|search|show.*(product|item)|in stock", t):
        add_score(Intent.PRODUCT_SEARCH, 1.0)

    # Human agent
    if re.search(r"\b(agent|human|representative|speak to|call|phone|email support)\b", t):
        add_score(Intent.ESCALATE, 1.0)

    # Greetings / goodbyes
    if re.search(r"\b(hi|hello|hey|good (morning|afternoon|evening))\b", t):
        add_score(Intent.GREETING, 0.8)
    if re.search(r"\b(bye|goodbye|see you|thanks|thank you)\b", t):
        add_score(Intent.GOODBYE, 0.8)

    # FAQs
    if re.search(r"\bhours|open|warranty|payment|discount|coupon|support\b", t):
        add_score(Intent.FAQ, 0.6)

    if not score_map:
        return (Intent.UNKNOWN, 0.0, {})

    best_intent = max(score_map.items(), key=lambda x: x[1])[0]
    best_score = score_map[best_intent]
    return (best_intent, min(best_score, 1.0), {})

