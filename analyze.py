import base64
import requests
import json
import os

with open("app/src/main/res/drawable/sample_outfit.png", "rb") as image_file:
    encoded_string = base64.b64encode(image_file.read()).decode('utf-8')

prompt = """
Analyze this outfit. 
Return ONLY a valid JSON string (no markdown, no backticks).
{
    "overallScore": 86,
    "colorHarmonyScore": 88,
    "fitScore": 85,
    "styleScore": 87,
    "shortTitle": "Beautiful",
    "shortDescription": "A cozy and stylish look.",
    "primaryClothingItem": "Brown Hoodie",
    "styleTags": ["Casual", "Streetwear"],
    "bestForOccasions": ["Casual Outing", "School/College", "Weekend Brunch"],
    "whatLooksBest": "...",
    "whatCouldImprove": "...",
    "outfitElements": ["Brown Hoodie", "Jeans"],
    "detectedColors": ["#8B4513", "#FFFFFF"],
    "colorsDescription": "Warm earthy tones."
}
"""

payload = {
  "model": "openai/gpt-5.4-image",
  "messages": [
    {
      "role": "user",
      "content": [
        { "type": "text", "text": prompt },
        { "type": "image_url", "image_url": { "url": f"data:image/png;base64,{encoded_string}" } }
      ]
    }
  ]
}

headers = {
  "Authorization": f"Bearer {os.environ.get('MESH_API_KEY', '')}",
  "Content-Type": "application/json"
}

response = requests.post("https://api.meshapi.ai/v1/chat/completions", json=payload, headers=headers)
try:
    content = response.json().get('choices', [{}])[0].get('message', {}).get('content', '')
    print(content)
except Exception as e:
    print(response.text)
