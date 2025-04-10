function setup() {
    createCanvas(800, 600);
}

function draw() {
    background(135, 206, 235);

    for (let y = height - height/3; y < height; y++) {
        for (let x = 0; x < width; x++) {
            let green = color(34, 139, 34);
            set(x, y, green);
        }
    }

    for (let i = 0; i < 1000; i++) {
        let randX = floor(random(0, width));
        let randY = floor(random(height - height/3, height));
        let randColor = color(random(0, 255), random(0, 255), random(0, 255));
        set(randX, randY, randColor);
    }

    for (let x = width/4; x < width - width/4; x++) {
        for (let y = height/3; y < height - height/3; y++) {
            let brown = color(125, 72, 26);
            set(x, y, brown);
        }
    }

    for (let y = 50, h = 0; y < height/3; y++, h += 2) {
        for (let x = width/2 - h; x <= width/2 + h; x++) {
            let red = color(212, 36, 36);
            set(x, y, red);
        }
    }

    updatePixels();
}
