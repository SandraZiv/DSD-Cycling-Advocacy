export function formatFloat(value) {
    return parseFloat(value).toFixed(2);
}

export function buildDuration(start, end) {
    let seconds = (new Date(end) - new Date(start))/1000;
    let hours =  zeroFill(Math.floor(seconds/3600));
    seconds = seconds % 3600;
    let minutes = zeroFill(Math.floor(seconds/60));
    seconds = zeroFill(Math.round(seconds % 60));

    return `${hours}:${minutes}:${seconds}`
}

function zeroFill(number, width = 2) {
    width -= number.toString().length;
    if (width > 0) {
        return new Array(width + (/\./.test(number) ? 2 : 1)).join('0') + number;
    }
    return number + ""; // always return a string
}
