const img = document.getElementById('frame') as HTMLImageElement;
const stats = document.getElementById('stats') as HTMLDivElement;
let fps = 12;
setInterval(() => {
  stats.innerText = `FPS: ${fps}`;
}, 500);
