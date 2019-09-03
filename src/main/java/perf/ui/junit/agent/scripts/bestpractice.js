(function(){function bestPracticeCharset(){var charSet=document.characterSet;return charSet}
function bestPracticeDoctype(){var docType=document.doctype===null
var name=document.doctype.name
var systemID=document.doctype.systemId
return[docType,name,systemID]}
function bestPracticeHttpsH2(){function getConnectionType(){if(window.performance.getEntriesByType('navigation')&&window.performance.getEntriesByType('navigation')[0]&&window.performance.getEntriesByType('navigation')[0].nextHopProtocol){return window.performance.getEntriesByType('navigation')[0].nextHopProtocol}else if(window.performance&&window.performance.getEntriesByType&&window.performance.getEntriesByType('resource')){var resources=window.performance.getEntriesByType('resource');if(resources.length>1&&resources[0].nextHopProtocol){var host=document.domain;for(var i=0,len=resources.length;i<len;i++){if(host===util.getHostname(resources[i].name)){return resources[i].nextHopProtocol}}}}
return'unknown'}
var url=document.URL
var connectionType=getConnectionType()
return{'url':url,'connectionType':connectionType}}
function bestPracticeLanguage(){var html=document.getElementsByTagName('html');var language=html[0].getAttribute('lang')
return{'length':html.length,'lang':language}}
function bestPracticeMetaDescription(){var metas=Array.prototype.slice.call(document.querySelectorAll('meta[name][content]'));function caseInsensitiveAttributeValueFilter(attributeName,attributeValue){return function(item){var attribute=item.getAttribute(attributeName)||'';if(attribute.toLowerCase()===attributeValue.toLowerCase()){return item}
return undefined}}
metas=metas.filter(caseInsensitiveAttributeValueFilter('name','description'))
var description=metas.length>0?metas[0].getAttribute('content'):'';return description}
function bestPracticeOpimizely(){function getAbsoluteURL(url){var a=window.document.createElement('a');a.href=url;return a.href}
function getHostname(url){var a=window.document.createElement('a');a.href=url;return a.hostname}
function getSynchJSFiles(parentElement){var scripts=Array.prototype.slice.call(parentElement.getElementsByTagName('script'));return scripts.filter(function(s){return!s.async&&s.src&&!s.defer}).map(function(s){return getAbsoluteURL(s.src)})}
var bestPracticeOpimizely=[]
var scripts=getSynchJSFiles(document.head);scripts.forEach(function(script){bestPracticeOpimizely.push({'script':script,'hostname':getHostname(script)})})
return bestPracticeOpimizely}
function bestPracticePageTitle(){var title=document.title;return title}
function bestPracticeSPDY(){function getConnectionType(){if(window.performance.getEntriesByType('navigation')&&window.performance.getEntriesByType('navigation')[0]&&window.performance.getEntriesByType('navigation')[0].nextHopProtocol){return window.performance.getEntriesByType('navigation')[0].nextHopProtocol}else if(window.performance&&window.performance.getEntriesByType&&window.performance.getEntriesByType('resource')){var resources=window.performance.getEntriesByType('resource');if(resources.length>1&&resources[0].nextHopProtocol){var host=document.domain;for(var i=0,len=resources.length;i<len;i++){if(host===util.getHostname(resources[i].name)){return resources[i].nextHopProtocol}}}}
return'unknown'}
var connectionType=getConnectionType();return connectionType}
function bestPracticePageURL(){return document.URL}
return{'bestPracticeCharset':bestPracticeCharset(),'bestPracticeDoctype':bestPracticeDoctype(),'bestPracticeHttpsH2':bestPracticeHttpsH2(),'bestPracticeLanguage':bestPracticeLanguage(),'bestPracticeMetaDescription':bestPracticeMetaDescription(),'bestPracticeOpimizely':bestPracticeOpimizely(),'bestPracticePageTitle':bestPracticePageTitle(),'bestPracticeSPDY':bestPracticeSPDY(),'bestPracticePageURL':bestPracticePageURL()}})()