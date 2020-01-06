export function formatFloat(value) {
    return parseFloat(value).toFixed(2);
}

export function buildDuration(start, end) {
    let seconds = (new Date(end) - new Date(start))/1000;
    let hours =  this.zeroFill(Math.floor(seconds/3600), 2);
    seconds = seconds % 3600;
    let minutes = this.zeroFill(Math.floor(seconds/60), 2);
    seconds = this.zeroFill(Math.round(seconds % 60), 2);

    return `${hours}:${minutes}:${seconds}`
}

export function zeroFill(number, width) {
    width -= number.toString().length;
    if (width > 0) {
        return new Array(width + (/\./.test(number) ? 2 : 1)).join('0') + number;
    }
    return number + ""; // always return a string
}
