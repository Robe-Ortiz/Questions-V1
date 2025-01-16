function randomInRange(min, max) {
    return Math.random() * (max - min) + min;
}

(function frame() {
    confetti({
        particleCount: 1,
        startVelocity: 0,
        ticks: 200,
        origin: {
            x: Math.random(),
            y: Math.random() - 0.2
        },
        colors: ['#ffffff'],
        shapes: ['circle'],
        gravity: randomInRange(0.4, 0.6),
        scalar: randomInRange(0.4, 1),
        drift: randomInRange(-0.4, 0.4)
    });
    requestAnimationFrame(frame);
}());