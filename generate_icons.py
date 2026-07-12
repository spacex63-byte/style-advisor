from PIL import Image
import os

logo_path = 'app/src/main/res/drawable/app_logo.png'
if not os.path.exists(logo_path):
    print("Logo not found")
    exit(1)

img = Image.open(logo_path).convert("RGBA")

# Sizes for Android icons
sizes = {
    'mdpi': 48,
    'hdpi': 72,
    'xhdpi': 96,
    'xxhdpi': 144,
    'xxxhdpi': 192
}

for density, size in sizes.items():
    # Regular icon
    out_dir = f'app/src/main/res/mipmap-{density}'
    os.makedirs(out_dir, exist_ok=True)
    
    resized = img.resize((size, size), Image.Resampling.LANCZOS)
    resized.save(f'{out_dir}/ic_launcher.png')
    
    # Round icon (masking it to a circle)
    # create a circular mask
    mask = Image.new('L', (size, size), 0)
    from PIL import ImageDraw
    draw = ImageDraw.Draw(mask)
    draw.ellipse((0, 0, size, size), fill=255)
    
    round_img = Image.new('RGBA', (size, size))
    round_img.paste(resized, (0, 0), mask=mask)
    round_img.save(f'{out_dir}/ic_launcher_round.png')

print("Icons generated successfully!")
