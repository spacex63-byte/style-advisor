import os

replacements = {
    "ThemeCyberPurple": "ThemeLightBlue",
    "TextCyberWhite": "TextNavyBlue",
    "PrimaryNeonBerry": "PrimaryBlue",
    "ButtonBerryStart": "ButtonBlueStart",
    "ButtonBerryEnd": "ButtonBlueEnd",
    "BackgroundCyberDark": "BackgroundCoolWhite",
    "BerryPillBg": "BluePillBg",
    "ScoreTextBerry": "ScoreTextBlue"
}

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()
    
    new_content = content
    for old, new in replacements.items():
        new_content = new_content.replace(old, new)
        
    if content != new_content:
        with open(filepath, 'w') as f:
            f.write(new_content)
        print(f"Updated {filepath}")

for root, dirs, files in os.walk('app/src/main/java/com/example/styleadvisor'):
    for file in files:
        if file.endswith('.kt'):
            process_file(os.path.join(root, file))
