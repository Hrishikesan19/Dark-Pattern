from fastapi import FastAPI
from pydantic import BaseModel
from transformers import pipeline
import uvicorn

app = FastAPI()
classifier = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")

class InputText(BaseModel):
    text: str

labels = [
    "false urgency", "confirm shaming", "basket sneaking",
    "forced action", "bait and switch", "drip pricing",
    "subscription trap", "interface interference", "nagging", "disguised advertisement"
]

@app.post("/classify")
async def classify(input: InputText):
    result = classifier(input.text, labels)
    return {
        "text": input.text,
        "label": result["labels"][0],
        "confidence": round(result["scores"][0], 2)
    }

if __name__ == "__main__":
    uvicorn.run(app, port=8000)