export function scrollToBottom(element) {
    element.scrollIntoView(false)
    element.scrollTop = element.scrollHeight
}