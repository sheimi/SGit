colorDiff = (index) ->
  diff = CodeLoader.getDiff index

  if not diff
    return

  changeType = CodeLoader.getChangeType index

  if changeType == 'ADD'
    path = CodeLoader.getNewPath index
  else
    path = CodeLoader.getOldPath index

  highlighted = hljs.highlight "diff", diff, true
  diffContent = highlighted.value.split '\n'
  codes = diffContent[4..].join '\n'

  diffBlock = $('<div>', {class:'diff-block'})
  infoBlock = $('<div>', {class:'info-block'})
  pathBlock = $('<div>', {class:'path-block'}).text path
  changeTypeBlock = $('<div>', {class:'change-type'}).text changeType
  infoBlock.append changeTypeBlock
  infoBlock.append pathBlock

  codeBlock = $('<code>').html codes
  codeBlock = $('<pre>', {class:"diff"}).append codeBlock
  diffBlock.append infoBlock
  diffBlock.append codeBlock
  $('body').append diffBlock

commitInfo = () ->
  if not CodeLoader.haveCommitInfo()
     return
  commitMessage = CodeLoader.getCommitMessage()
  commitInfo = CodeLoader.getCommitInfo()

  diffBlock = $('<div>', {class:'diff-block'})

  infoBlock = $('<div>', {class:'info-block'})
  commitInfoBlock = $('<pre>', {class:'commitinfo-block'}).text commitInfo
  infoBlock.append commitInfoBlock

  diffBlock.append infoBlock

  codeBlock = $('<code>').text commitMessage
  codeBlock = $('<pre>', {class:"diff"}).append codeBlock
  diffBlock.append codeBlock
  $('body').append diffBlock

window.notifyEntriesReady = () ->
  commitInfo()
  length = CodeLoader.getDiffSize()
  for index in [0..length-1] by 1
    colorDiff index

$(document).ready ()->
  CodeLoader.getDiffEntries()
