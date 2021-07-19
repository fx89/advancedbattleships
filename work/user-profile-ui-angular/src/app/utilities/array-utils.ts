export function arrayDiff(subtractFromArr : any[], subtractArr : any[], comparator : Function) {
    if (subtractFromArr == null || subtractFromArr == undefined) {
        return null;
    }

    if (subtractArr == null || subtractArr == undefined) {
        return Object.assign([], subtractFromArr);
    }

    let ret : any[] = [];

    subtractFromArr.forEach(subtractFrom => {
        let identifiedElement : any = null;

        subtractArr.forEach(subtract => {
            if (comparator(subtractFrom, subtract)) {
                identifiedElement = subtract;
            }
        });

        if (identifiedElement == null) {
            ret.push(subtractFrom);
        }
    });

    return ret;
}