import os

replacements = {
    "BackgroundCyberDark": "BackgroundCoolWhite",
    "ThemeCyberPurple": "ThemeLightBlue",
    "PrimaryNeonBerry": "PrimaryBlue",
    "TextCyberWhite": "TextNavyBlue",
    "ButtonBerryStart": "ButtonBlueStart",
    "ButtonBerryEnd": "ButtonBlueEnd",
    "ButtonIconBerryBg": "ButtonIconBlueBg",
    "ButtonIconBerryTint": "ButtonIconBlueTint",
    "PromoCyberStart": "PromoBlueStart",
    "PromoCyberEnd": "PromoBlueEnd",
    "NavIconActiveBerryBg": "NavIconActiveBlueBg",
    "NavIconActiveBerryTint": "NavIconActiveBlueTint",
    "NavIconInactiveBerryTint": "NavIconInactiveBlueTint",
    "FabBerryBg": "FabBlueBg",
    "ScoreTextBerry": "ScoreTextBlue",
    "BerryPillBg": "BluePillBg",
    "BerryPillText": "BluePillText",
    "TabActiveBerryText": "TabActiveBlueText",
    "TabInactiveBerryText": "TabInactiveBlueText"
}

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    new_content = content
    for cyber, blue in replacements.items():
        new_content = new_content.replace(cyber, blue)
        
    if content != new_content:
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk('app/src/main/java/com/example/styleadvisor'):
    for file in files:
        if file.endswith('.kt'):
            process_file(os.path.join(root, file))
