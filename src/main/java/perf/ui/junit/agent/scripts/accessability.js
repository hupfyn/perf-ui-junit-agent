(function(){function accessAltImage(){var images=Array.prototype.slice.call(window.document.getElementsByTagName('img'))
var offending=[];var missing=0;var score=0;var tooLong=0;var unique={};images.forEach(function(image){if(!image.alt||image.alt===''){score+=10;missing++;if(image.src){offending.push(image.src);unique[image.src]=1}}else if(image.alt&&image.alt.length>125){score+=5;offending.push(image.src);tooLong++}})
return[score,unique,missing,tooLong]}
function accessHeadings(){var headings=['h6','h5','h4','h3','h2','h1'];var headingData=[]
headings.forEach(function(heading){var entry=window.document.getElementsByTagName(heading).length
headingData.push({'type':heading,'entry':entry})})
return headingData}
function accessLabelOnInput(){function getMatchingLabel(id,labels){return labels.filter(function(entry){return entry.attributes.for&&entry.attributes.for.value===id})}
function hasLabel(input,labels){return input.id&&getMatchingLabel(input.id,labels).length>0}
function isExcluded(input,excludedInputTypes){return excludedInputTypes.includes(input.type)}
function isInsideLabel(input){return input.parentElement.nodeName==='LABEL'}
var labels=Array.prototype.slice.call(window.document.getElementsByTagName('label'));var score=0;var offending=[];var inputs=Array.prototype.slice.call(window.document.querySelectorAll('input, textarea, select'));var excludedInputTypes=['button','hidden','image','reset','submit'];inputs.forEach(function(input){if(isExcluded(input,excludedInputTypes)||isInsideLabel(input)||hasLabel(input,labels)){return}
offending.push(input.id||input.name||input.outerHTML);score+=10});return[score,offending]}
function accessLandmark(){var landmarks=['article','aside','footer','header','nav','main'];var totalLandmarks=0;landmarks.forEach(function(landmark){totalLandmarks+=Array.prototype.slice.call(window.document.getElementsByTagName(landmark)).length});return totalLandmarks}
function accessNeverSuppressZoom(){var metas=Array.prototype.slice.call(document.querySelectorAll('meta[name][content]'));function caseInsensitiveAttributeValueFilter(){return function(item){var attribute=item.getAttribute('name')||'';if(attribute.toLowerCase()==='viewport'){return item}
return undefined}}
var metasData=[]
metas=metas.filter(caseInsensitiveAttributeValueFilter());metas.forEach(function(meta){metasData.push({'content':meta.content})})
return metasData}
function accessSection(){var headings=['h6','h5','h4','h3','h2','h1'];var sections=Array.prototype.slice.call(window.document.getElementsByTagName('section'));var sectionData=[]
sections.forEach(function(section){var tagsData=[]
headings.forEach(function(heading){tagsData.push([heading,section.getElementsByTagName(heading).length])});sectionData.push([tagsData])});return sectionData}
function accessTable(){var tables=Array.prototype.slice.call(window.document.getElementsByTagName('table'));var tableData=[]
tables.forEach(function(table){if(table.getElementsByTagName('caption').length===0){tableData.push(table.id)}
var trs=table.getElementsByTagName('tr');if(trs[0]&&trs[0].getElementsByTagName('th').length===0){tableData.push(table.id)}})
return tableData}
return{'altImage':accessAltImage(),'heading':accessHeadings(),'labelOnInput':accessLabelOnInput(),'landmark':accessLandmark(),'neverSuppressZoom':accessNeverSuppressZoom(),'section':accessSection(),'table':accessTable()}})()