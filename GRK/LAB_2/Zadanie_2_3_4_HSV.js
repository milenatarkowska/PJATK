function preload() {
    img = loadImage("https://raw.githubusercontent.com/scikit-image/scikit-image/master/skimage/data/astronaut.png");
    img_h = createImage(256, 256); // hue
    img_s = createImage(256, 256); // saturation
    img_v = createImage(256, 256); // value
}

function setup() {
    createCanvas(512, 512);
    img.resize(256, 256);

    img.loadPixels();
    img_h.loadPixels();
    img_s.loadPixels();
    img_v.loadPixels();

    for (let x = 0; x < img.width; x++) {
        for (let y = 0; y < img.height; y++) {

            let pos = 4 * (y * img.width + x); // Pozycja piksela w tablicy

            // Normalizacja kolorów
            let r = img.pixels[pos] / 255;
            let g = img.pixels[pos + 1] / 255;
            let b = img.pixels[pos + 2] / 255;

            // Max i min skladowa
            let cmax = Math.max(r, g, b);
            let cmin = Math.min(r, g, b);

            // value
            let v = cmax;

            img_v.set(x, y, 255*v);

            l = (cmax+cmin)/2; //lightness
            c = cmax - cmin //chromatycznosc
            s=c/(1-Math.abs(2*l-1)); //saturacja

            img_s.set(x, y, 255*s);

            // obliczanie hue
            if(c==0){
                h=0;
            } else if(v==r) {
                h=((g-b)/c)%6;
            } else if(v==g) {
                h=((b-r)/c)+2;
            } else { /*v==b*/
                h=((r-g)/c)+4;
            }
            h /= 6;

            if(h<0) h+=1;

            img_h.set(x, y, 255*h);
        }
    }

    img_h.updatePixels();
    img_s.updatePixels();
    img_v.updatePixels();
    img.updatePixels();
}

function draw() {
    background(256);
    image(img_h, 0, 0);
    image(img_s, 256, 0);
    image(img_v, 0, 256);
    image(img, 256, 256);
}