function setup() {
    createCanvas(800, 600);
}

function draw() {
    background(220);

    for(y=0; y<height; y++)
        for(x=0; x<width; x++) {

            colorNormLin = (x+y)/(width+height) * 256;
            set(x, y, color(0, 0, colorNormLin))

            xDist = x - (width/2);
            yDist = y - (height/2);
            colorNormCirc = sqrt(pow(xDist, 2) + pow(yDist, 2));

            set(x, y, color((255 - colorNormCirc),colorNormCirc, colorNormLin));
        }




    updatePixels();
}