import os
import gradio as gr

from ecom_bot.bot import EcomCustomerServiceBot


bot = EcomCustomerServiceBot()


def respond(message, history):
    return bot.respond(message, history or [])


demo = gr.ChatInterface(
    fn=respond,
    title="E-commerce Customer Service Chatbot",
    description=(
        "Ask about order status, returns/refunds, shipping, products, or policies. "
        "No personal data is stored."
    ),
)


if __name__ == "__main__":
    demo.launch(server_name="0.0.0.0", server_port=int(os.getenv("PORT", "7860")))

