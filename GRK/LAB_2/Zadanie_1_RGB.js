function preload() {
    img = loadImage("https://raw.githubusercontent.com/scikit-image/scikit-image/master/skimage/data/astronaut.png");

    img_r = createImage(256,256);
    img_g = createImage(256,256);
    img_b = createImage(256,256);
    img_sum = createImage(256,256);
}
function setup() {
    createCanvas(512,512);
    img.resize(256,256);

    img.loadPixels();
    img_r.loadPixels();
    img_g.loadPixels();
    img_b.loadPixels();


    for (let x = 0; x < img.width; x++) {
        for (let y = 0; y < img.height; y++) {

            let pos = 4 * (y * img.width + x); //pozycja pixela w tablicy

            /* pos - wartosc czerwonego
               pos + 1 - wartosc zielonego
               pos + 2 - wartosc niebieskiego
               pos + 3 - wartosc alpha */

            let r = img.pixels[pos];
            let g = img.pixels[pos + 1];
            let b = img.pixels[pos + 2];

            img_r.pixels[pos] = r;
            img_r.pixels[pos + 1] = 0;
            img_r.pixels[pos + 2] = 0;
            img_r.pixels[pos + 3] = 255;

            img_g.pixels[pos] = 0;
            img_g.pixels[pos + 1] = g;
            img_g.pixels[pos + 2] = 0;
            img_g.pixels[pos + 3] = 255;

            img_b.pixels[pos] = 0;
            img_b.pixels[pos + 1] = 0;
            img_b.pixels[pos + 2] = b;
            img_b.pixels[pos + 3] = 255;
        }
    }

    img_r.updatePixels();
    img_g.updatePixels();
    img_b.updatePixels();

    img_sum.blend(img_r, 0, 0, 256, 256, 0, 0, 256, 256, ADD);
    img_sum.blend(img_g, 0, 0, 256, 256, 0, 0, 256, 256, ADD);
    img_sum.blend(img_b, 0, 0, 256, 256, 0, 0, 256, 256, ADD);
}

function draw(){
    background(0);
    image(img_r, 0, 0);
    image(img_g, 256, 0);
    image(img_b, 0, 256);
    image(img_sum, 256, 256);
}

