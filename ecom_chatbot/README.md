## E-commerce Customer Service Chatbot

A simple customer service chatbot for e-commerce use-cases. It can answer questions about orders, returns/refunds, shipping, products, FAQs, and optionally use an LLM as a fallback.

### Features
- Intent detection (order status, returns, refunds, shipping, product search, FAQs)
- Rule-based handlers with sample data
- Optional LLM fallback via OpenAI (set `OPENAI_API_KEY`)
- Gradio web UI

### Quickstart
1. Create and activate a virtual environment
```bash
python3 -m venv .venv
source .venv/bin/activate
```

2. Install dependencies
```bash
pip install -r requirements.txt
```

3. (Optional) Set your OpenAI key for LLM fallback
```bash
export OPENAI_API_KEY="sk-..."
# Optional model override
export OPENAI_MODEL="gpt-4o-mini"
```

4. Run the app
```bash
python app.py
```

5. Open the link printed in the console (defaults to `http://0.0.0.0:7860`).

### Project Structure
```
ecom_chatbot/
  app.py
  requirements.txt
  README.md
  ecom_bot/
    __init__.py
    bot.py
    intents.py
    handlers.py
    data/
      products.json
      faqs.json
      policies.json
      orders.json
```

### Notes
- The bot uses only local data unless an OpenAI key is provided, in which case it uses the LLM as a final fallback for unknown queries.
- Sample order IDs are 5-digit numbers like `10001`.

