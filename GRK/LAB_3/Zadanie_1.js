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
    pixels[idx]=c;
    pixels[idx+1]=c;
    pixels[idx+2]=c;
    pixels[idx+3]=255;
}

//algorytm rysowania linii
function draw_line() {

    let dx = x1 - x0; //dlugosc x
    let dy = y1 - y0; //dlugosc y

    let a = dy/dx;  //przyrost y wzgledem x
    let b = y0 - a*x0; //stala b

    for(let x = x0 ; x <= x1 ; x++){
        let y = Math.round(a * x + b);
        set_pixel(x,y, color(0));
    }
}
