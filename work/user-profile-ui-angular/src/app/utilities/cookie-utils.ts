export function absGetCookie(cName) {
    const cDecoded = decodeURIComponent(document.cookie);
    const cArr = cDecoded.split('; ');
    const name = cName.replace("=", "&eq") + "=";

    var ret;
    cArr.forEach(val => {
        if (val.indexOf(name) === 0) {
            ret = val.substring(name.length);
        } 
    })

    return ret;
}

export function absSetCokie(name, value, days) {
    var expires = "";
    var maxAge = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
        maxAge = "; max-age=" + date.toUTCString();
    }
    document.cookie = name.replace("=", "&eq") + "=" + (value || "")  + expires + maxAge + "; path=/";
}