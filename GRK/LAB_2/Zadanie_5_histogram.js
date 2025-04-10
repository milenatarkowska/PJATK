let histogram = new Array(256).fill(0);

function preload() {
    img = loadImage("https://raw.githubusercontent.com/scikit-image/scikit-image/master/skimage/data/astronaut.png");
}

function setup() {
    createCanvas(512, 256);
    img.filter('gray');

    img.loadPixels();

    for (let x = 0; x < img.width; x++) {
        for (let y = 0; y < img.height; y++) {

            let pos = 4 * (y * img.width + x);

            let colorValue = img.pixels[pos];

            histogram[colorValue]++;

        }
    }
}

function draw() {
    background(255);
    stroke(0);
    image(img, 256, 0, 256, 256);

    for (let i = 0; i < 256; i++) {
        line(i, height, i, height - histogram[i] / 10);
    }
}
