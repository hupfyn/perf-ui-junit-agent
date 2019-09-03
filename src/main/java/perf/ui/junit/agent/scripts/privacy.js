(function(){function privacyAMP(){var score=100;var html=document.getElementsByTagName('html')[0];if((html&&html.getAttribute('amp-version'))||window.AMP){score=0}
return score}
function privacyFacebook(){return window.FB?0:100}
function privacyGoogleAnalytics(){return(window.ga&&window.ga.create)?0:100}
function privacyHTTPS(){var url=document.URL;var score=100;var message='';if(url.indexOf('https://')===-1){score=0;message='What!! The page is not using HTTPS. '+'Every unencrypted HTTP request reveals information about user’s behavior, read more about it at https://https.cio.gov/everything/. '+'You can get a totally free SSL/TLS certificate from https://letsencrypt.org/.'}
return[score,message]}
function privacySurveillance(){var score=100;var docDomain=document.domain;var offending=[];var offenders=['.google.','facebook.com','youtube.','yahoo.com'];for(var i=0;i<offenders.length;i++){if(docDomain.indexOf(offenders[i])>-1){score=0;offending.push(docDomain)}}
return[score,offending,docDomain]}
function privacyYouTube(){return window.YT?0:100}
return{'privacyAMP':privacyAMP(),'privacyFacebook':privacyFacebook(),'privacyGoogleAnalytics':privacyGoogleAnalytics(),'privacyHTTPS':privacyHTTPS(),'privacySurveillance':privacySurveillance(),'privacyYouTube':privacyYouTube()}})()