(function(){function performanceTiming(){return performance.timing}
function performanceResources(){return performance.getEntriesByType("resource")}
function performanceMark(){return performance.getEntriesByType("mark")}
function performanceMeasure(){return performance.getEntriesByType("measure")}
return{'performanceTiming':performanceTiming(),'performanceResources':performanceResources(),'performanceMark':performanceMark(),'performanceMeasure':performanceMeasure()}})()