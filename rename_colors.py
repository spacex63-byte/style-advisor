import os

replacements = {
    "BackgroundCoolWhite": "BackgroundCyberDark",
    "ThemeLightBlue": "ThemeCyberPurple",
    "PrimaryBlue": "PrimaryNeonBerry",
    "TextNavyBlue": "TextCyberWhite",
    "ButtonBlueStart": "ButtonBerryStart",
    "ButtonBlueEnd": "ButtonBerryEnd",
    "ButtonIconBlueBg": "ButtonIconBerryBg",
    "ButtonIconBlueTint": "ButtonIconBerryTint",
    "PromoBlueStart": "PromoCyberStart",
    "PromoBlueEnd": "PromoCyberEnd",
    "NavIconActiveBlueBg": "NavIconActiveBerryBg",
    "NavIconActiveBlueTint": "NavIconActiveBerryTint",
    "NavIconInactiveBlueTint": "NavIconInactiveBerryTint",
    "FabBlueBg": "FabBerryBg",
    "ScoreTextBlue": "ScoreTextBerry",
    "BluePillBg": "BerryPillBg",
    "BluePillText": "BerryPillText",
    "TabActiveBlueText": "TabActiveBerryText",
    "TabInactiveBlueText": "TabInactiveBerryText"
}

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    new_content = content
    for old, new in replacements.items():
        new_content = new_content.replace(old, new)
        
    if new_content != content:
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Updated {filepath}")

for root, _, files in os.walk("app/src/main/java"):
    for file in files:
        if file.endswith(".kt"):
            process_file(os.path.join(root, file))
