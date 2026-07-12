from PIL import Image
import os

new_logo_path = 'app/src/main/res/drawable/app_logo_new.png'
foreground_path = 'app/src/main/res/drawable/ic_launcher_foreground.png'

img = Image.open(new_logo_path).convert("RGBA")
canvas_size = max(img.size)

# The user felt it was scaled down. Let's make it larger. 
# 72/108 is the safe zone (66.6%). If we scale it to 75% it will slightly bleed out of the safe zone
# but look larger. Let's try 70%.
scale_factor = 0.75
padded_img = Image.new("RGBA", (canvas_size, canvas_size), (0, 0, 0, 0))

new_w = int(img.width * scale_factor)
new_h = int(img.height * scale_factor)
resized_original = img.resize((new_w, new_h), Image.Resampling.LANCZOS)

offset_x = (canvas_size - new_w) // 2
offset_y = (canvas_size - new_h) // 2
padded_img.paste(resized_original, (offset_x, offset_y))

# Save for adaptive icon
padded_img = padded_img.resize((1024, 1024), Image.Resampling.LANCZOS)
padded_img.save(foreground_path)

# Also generate legacy mipmap icons using this slightly larger scale
# For legacy icons, the safe zone is the whole icon, so we don't want too much padding. 
# We'll use a scale factor of 0.9 for legacy icons so they are large.
legacy_scale = 0.9
legacy_padded = Image.new("RGBA", (canvas_size, canvas_size), (0, 0, 0, 0))
leg_w = int(img.width * legacy_scale)
leg_h = int(img.height * legacy_scale)
leg_resized = img.resize((leg_w, leg_h), Image.Resampling.LANCZOS)
legacy_padded.paste(leg_resized, ((canvas_size - leg_w)//2, (canvas_size - leg_h)//2))

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
    
    resized = legacy_padded.resize((size, size), Image.Resampling.LANCZOS)
    resized.save(f'{out_dir}/ic_launcher.png')
    
    round_img = Image.new('RGBA', (size, size), (255, 255, 255, 255))
    round_img.paste(resized, (0, 0), mask=resized)
    
    mask = Image.new('L', (size, size), 0)
    from PIL import ImageDraw
    draw = ImageDraw.Draw(mask)
    draw.ellipse((0, 0, size, size), fill=255)
    
    final_round = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    final_round.paste(round_img, (0, 0), mask=mask)
    
    final_round.save(f'{out_dir}/ic_launcher_round.png')

print("Foreground and mipmaps updated")
