let p1x = 10;
let p1y = 40;

let p2x = 20;
let p2y = 50;

let p3x = 30;
let p3y = 60;

let cx;
let cy;

function setup(){
    createCanvas(800, 600);
    frameRate(25);
}

function draw(){
    background(0);
    stroke(255);

    let randNum = floor(random(0, 10));
    if(randNum < 5){
        p1x += randNum * 2;
        p1y += randNum * 2;
        p2x -= randNum * 2;
        p2y -= randNum * 2;
        p3x += randNum * 2;
        p3y -= randNum * 2;
    } else {
        p1x -= randNum * 2;
        p1y -= randNum * 2;
        p2x += randNum * 2;
        p2y += randNum * 2;
        p3x -= randNum * 2;
        p3y += randNum * 2;
    }

    point(p1x, p1y);
    point(p2x, p2y);
    point(p3x, p3y);

    cx = p1x;
    cy = p1y;

    for(let i = 0 ; i < 30000 ; i++){
        let rd = floor(random(0, 3));
        switch(rd){
            case 0:
                cx = (cx + p1x)/2;
                cy = (cy + p1y)/2;
                break;
            case 1:
                cx = (cx + p2x)/2;
                cy = (cy + p2y)/2;
                break;
            default:
                cx = (cx + p3x)/2;
                cy = (cy + p3y)/2;
                break;
        }
        point(cx, cy);
    }
    updatePixels();
}
