String.prototype.rtrim = () ->
  return this.replace /\s+$/,''

lang = undefined
editor = undefined

displayFileContent = () ->
  rawCodes = CodeLoader.getCode()
  $('#editor').text rawCodes
  editorElm = document.getElementById "editor"
  editorOption =
    lineNumbers: true
    mode: lang
    theme: CodeLoader.getTheme()
    matchBrackets: true
    lineWrapping: true
    readOnly: true
  editor = CodeMirror.fromTextArea editorElm, editorOption

window.setLang = (l) ->
  lang = l
  if editor
    editor.setOption "mode", lang

window.display = displayFileContent

window.setEditable= () ->
  editor.setOption "readOnly", false

window.save = () ->
  editor.setOption "readOnly", true
  value = editor.getValue()
  CodeLoader.save(value)

window.copy_all = () ->
  value = editor.getValue().rtrim()
  return CodeLoader.copy_all(value)

$(document).ready ()->
  CodeLoader.loadCode()
