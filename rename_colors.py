import os
import glob

replacements = {
    "BackgroundWarmWhite": "BackgroundCoolWhite",
    "LightPeachyBg": "ThemeLightBlue",
    "CoralAccent": "PrimaryBlue",
    "TextDarkMaroon": "TextNavyBlue",
    "ButtonGradientStart": "ButtonBlueStart",
    "ButtonGradientEnd": "ButtonBlueEnd",
    "ButtonIconBg": "ButtonIconBlueBg",
    "ButtonIconTint": "ButtonIconBlueTint",
    "PromoGradientStart": "PromoBlueStart",
    "PromoGradientEnd": "PromoBlueEnd",
    "NavIconActiveBg": "NavIconActiveBlueBg",
    "NavIconActiveTint": "NavIconActiveBlueTint",
    "NavIconInactiveTint": "NavIconInactiveBlueTint",
    "FabBg": "FabBlueBg",
    "ScoreTextRed": "ScoreTextBlue",
    "RedPillBg": "BluePillBg",
    "RedPillText": "BluePillText",
    "TabActiveText": "TabActiveBlueText"
}

for filepath in glob.glob('app/src/main/java/**/*.kt', recursive=True):
    with open(filepath, 'r') as f:
        content = f.read()
    
    original = content
    for old, new in replacements.items():
        content = content.replace(old, new)
        
    if original != content:
        with open(filepath, 'w') as f:
            f.write(content)
        print(f"Updated {filepath}")
