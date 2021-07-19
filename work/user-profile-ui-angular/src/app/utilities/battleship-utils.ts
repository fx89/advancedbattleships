export function copyHullArray(hullArray : boolean[][]) : boolean[][] {
    let ret : boolean[][] = [];

    hullArray.forEach(row => {
        let newRow : boolean[] = Object.assign([], row);
        ret.push(newRow);
    });

    return ret;
}