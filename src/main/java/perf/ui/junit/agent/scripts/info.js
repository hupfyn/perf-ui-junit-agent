(function(){function infoAmp(){var html=document.getElementsByTagName('html')[0];var isAmp=!1
if((html&&html.getAttribute('amp-version'))||window.AMP){isAmp=!0}
return isAmp}
function infoBrowser(){var browser='unknown';var match=window.navigator.userAgent.match(/(Chrome|Firefox|Safari)\/(\S+)/);browser=match?match[1]+' '+match[2]:browser;return browser}
function infoConnectionType(){util={getConnectionType:function(){if(window.performance.getEntriesByType('navigation')&&window.performance.getEntriesByType('navigation')[0]&&window.performance.getEntriesByType('navigation')[0].nextHopProtocol){return window.performance.getEntriesByType('navigation')[0].nextHopProtocol}else if(window.performance&&window.performance.getEntriesByType&&window.performance.getEntriesByType('resource')){var resources=window.performance.getEntriesByType('resource');if(resources.length>1&&resources[0].nextHopProtocol){var host=document.domain;for(var i=0,len=resources.length;i<len;i++){if(host===util.getHostname(resources[i].name)){return resources[i].nextHopProtocol}}}}
return'unknown'},getHostname:function(url){var a=window.document.createElement('a');a.href=url;return a.hostname},}
return util.getConnectionType()}
function infoDocumentHeights(){return Math.max(document.body.scrollHeight,document.body.offsetHeight,document.documentElement.clientHeight,document.documentElement.scrollHeight,document.documentElement.offsetHeight)}
function infoDocumentWidth(){return Math.max(document.body.scrollWidth,document.body.offsetWidth,document.documentElement.clientWidth,document.documentElement.scrollWidth,document.documentElement.offsetWidth)}
function infoDocumentTitle(){return document.title}
function infoDomDepth(){function domDepth(document){var allElems=document.getElementsByTagName('*');var allElemsLen=allElems.length;var totalParents=0;var maxParents=0;while(allElemsLen--){var parents=numParents(allElems[allElemsLen]);if(parents>maxParents){maxParents=parents}
totalParents+=parents}
var average=totalParents/allElems.length;return{avg:Math.round(average),max:maxParents}}
function numParents(elem){var n=0;if(elem.parentNode){while((elem=elem.parentNode)){n++}}
return n}
var depth=domDepth(document);return{avg:depth.avg,max:depth.max}}
function infoDomElements(){return document.getElementsByTagName('*').length}
function infoIFrame(){return document.getElementsByTagName('iframe').length}
function infoScripts(){return document.getElementsByTagName('script').length}
function infoLocalStorageSize(){function storageSize(storage){if(storage){var keys=storage.length||Object.keys(storage).length;var bytes=0;for(var i=0;i<keys;i++){var key=storage.key(i);var val=storage.getItem(key);bytes+=key.length+val.length}
return bytes}else{return 0}}
return storageSize(window.localStorage)}
function infoSessionStorageSize(){function storageSize(storage){var keys=storage.length||Object.keys(storage).length;var bytes=0;for(var i=0;i<keys;i++){var key=storage.key(i);var val=storage.getItem(key);bytes+=key.length+val.length}
return bytes}
return storageSize(window.sessionStorage)}
function infoThirdParty(){return{boomerang:window.BOOMR?window.BOOMR.version:!1,facebook:window.FB?!0:!1,gtm:window.google_tag_manager?!0:!1,ga:window.ga?!0:!1,jquery:window.jQuery?window.jQuery.fn.jquery:!1,newrelic:window.newrelic?!0:!1,matomo:window.Piwik?!0:window.Matomo?!0:!1}}
function infoResourceHints(){function getResourceHintsHrefs(type){var links=Array.prototype.slice.call(window.document.head.getElementsByTagName('link'));return links.filter(function(link){return link.rel===type}).map(function(link){return link.href})}
return{'dns-prefetch':getResourceHintsHrefs('dns-prefetch'),preconnect:getResourceHintsHrefs('preconnect'),prefetch:getResourceHintsHrefs('prefetch'),prerender:getResourceHintsHrefs('prerender')}}
return{'infoAmp':infoAmp(),'infoBrowser':infoBrowser(),'infoConnectionType':infoConnectionType(),'infoDocumentHeights':infoDocumentHeights(),'infoDocumentWidth':infoDocumentWidth(),'infoDocumentTitle':infoDocumentTitle(),'infoDomDepth':infoDomDepth(),'infoDomElements':infoDomElements(),'infoIFrame':infoIFrame(),'infoScripts':infoScripts(),'infoLocalStorageSize':infoLocalStorageSize(),'infoSessionStorageSize':infoSessionStorageSize(),'infoThirdParty':infoThirdParty(),'infoResourceHints':infoResourceHints()}})()