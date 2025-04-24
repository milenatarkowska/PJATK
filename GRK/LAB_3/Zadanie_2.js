function setup() {
    createCanvas(512,512);
    background(255);
}

//wspolrzedne poczatku i konca linii
var x0=-1;
var y0=-1;
var x1=-1;
var y1=-1;

//wybor wspolrzednych poczatkowych
function mousePressed() {
    x0=mouseX;
    y0=mouseY;
}

//rysowanie kolek dla wspolrzednych
function mouseDragged() {
    x1=mouseX;
    y1=mouseY;
    background(255);
    noStroke();
    fill('red');
    ellipse(x0-3,y0-3,6);
    fill('green');
    ellipse(x1-3,y1-3,6);
}

//kasuje ekran i uruchamia algorytm rysowania linii
function mouseReleased() {
    background(255);
    loadPixels();
    draw_line();
    updatePixels();
}

//zamalowuje pixel w odcien szarosci
function set_pixel(x,y,c) {
    idx=(y*512+x)*4;
    pixels[idx]= -c; // czerwony -dxy
    pixels[idx+1]=c;  // zielony normalne dxy
    pixels[idx+2]=0;  // niebieski 0
    pixels[idx+3]=255;
}

//algorytm rysowania linii Bresenhama
function draw_line() {

    let dx = x1 - x0; // Długość X
    let dy = y1 - y0; // Długość Y

    let a = dy / dx;
    let b = y0 - a * x0;

    for (let y = 0; y < height; y++) {
        for (let x = 0; x < width; x++) {
            let dxy = Math.round((dy/dx) * (x - x0) - (y - y0));
            dxy = dxy * 2 * dx;
            set_pixel(x,y, dxy);
        }
    }
}