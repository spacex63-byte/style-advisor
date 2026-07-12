from PIL import Image
import os

new_logo_path = 'app/src/main/res/drawable/app_logo_new.png'
old_logo_path = 'app/src/main/res/drawable/app_logo.png'

if not os.path.exists(new_logo_path):
    print("New logo not found")
    exit(1)

img = Image.open(new_logo_path).convert("RGBA")
print(f"Original size: {img.size}")

# We want to add padding so the logo doesn't look zoomed in.
# Let's say we scale the original image down to 60% of the new canvas, keeping it centered.
canvas_size = max(img.size)
padded_img = Image.new("RGBA", (canvas_size, canvas_size), (0, 0, 0, 0)) # transparent background

# Scale factor
scale_factor = 0.6
new_w = int(img.width * scale_factor)
new_h = int(img.height * scale_factor)
resized_original = img.resize((new_w, new_h), Image.Resampling.LANCZOS)

# Paste in center
offset_x = (canvas_size - new_w) // 2
offset_y = (canvas_size - new_h) // 2
padded_img.paste(resized_original, (offset_x, offset_y))

# Save the padded logo as app_logo.png (so splash screen and adaptive icon pick it up)
# We can make it 1024x1024 for high quality
padded_img = padded_img.resize((1024, 1024), Image.Resampling.LANCZOS)
padded_img.save(old_logo_path)
print("Saved padded app_logo.png")

# Sizes for Android icons
sizes = {
    'mdpi': 48,
    'hdpi': 72,
    'xhdpi': 96,
    'xxhdpi': 144,
    'xxxhdpi': 192
}

for density, size in sizes.items():
    out_dir = f'app/src/main/res/mipmap-{density}'
    os.makedirs(out_dir, exist_ok=True)
    
    resized = padded_img.resize((size, size), Image.Resampling.LANCZOS)
    resized.save(f'{out_dir}/ic_launcher.png')
    
    # Round icon (masking it to a circle with white background)
    round_img = Image.new('RGBA', (size, size), (255, 255, 255, 255))
    round_img.paste(resized, (0, 0), mask=resized)
    
    # create a circular mask
    mask = Image.new('L', (size, size), 0)
    from PIL import ImageDraw
    draw = ImageDraw.Draw(mask)
    draw.ellipse((0, 0, size, size), fill=255)
    
    final_round = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    final_round.paste(round_img, (0, 0), mask=mask)
    
    final_round.save(f'{out_dir}/ic_launcher_round.png')

print("Icons generated successfully!")
