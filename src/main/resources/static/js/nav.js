let nav_buttons = ["machine","shop","report"];

function setHeader() {
    let  headerContent = '';
    headerContent += '<div id="progress_loader_modal" class="progress-loader-modal"><div class="progress-loader-modal-content"></div></div>';
    headerContent += '<div style="text-align: center;"><h1>CGD Coffee Machine Module</h1></div><div class="topnav">';
    for (let i in nav_buttons){
        headerContent+= '<a id="'+nav_buttons[i]+'" onclick="onNavButtonClick('+i+')">'+nav_buttons[i].toUpperCase()+'</a>';
    }
    headerContent+= '</div>';
    document.getElementById('header_container').innerHTML = headerContent;
}

function onNavButtonClick(idx) {
    window.location.replace( baseURL+'/'+nav_buttons[idx]);
}

function menuButtonColorChange(idx) {
    try {
        for(let i in nav_buttons){
            document.getElementById(nav_buttons[i]).classList.remove("active");
        }
        document.getElementById(nav_buttons[idx]).classList.add("active");
    }catch (e) {}
}

function setFooter() {
    document.getElementById('footer_container').innerHTML =
        '<div class="bottom-bar">\n' +
        '<h3>Department of IT</h3>\n' +
        '<h2>Abul Khair Group</h2>\n' +
        '<h4>Corporate Office,D.T. Road,Pahartali,Chittagong</h4>\n' +
        '</div>'
}