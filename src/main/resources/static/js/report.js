function init() {
    setHeader();
    setFooter();
    menuButtonColorChange(2);
}

let currentReport = [];
let selectedTerritory = "";

function openReportModal(val) {
    let url = baseURL;
    if(val==1) {
        url+="/report/machine/summary";
        currentReport = ['report-table-machine-summary'];
    }
    else if(val==2) {
        url+="/report/shop/summary/location_wise";
        currentReport = ['report-table-division-wise',
            'report-table-region-wise','report-table-territory-wise'];
    }
    else if(val==3) {
        url+="/report/shop/summary/type_wise";
        currentReport = ['report-table-type-wise-shop'];
    }

    else if(val==4) {
        url+="/report/shop_machine_detail/location_wise";
        if(selectedTerritory.trim().length>0) url+="?territory="+selectedTerritory;
        currentReport = ['report-table-location-wise-shop-detail'];
        selectedTerritory = "";
    }

    callAPI(url,GET,null,reportCallback);
    function reportCallback(response){
        document.getElementById('report-modal-content').innerHTML = response;
        modalAction();
    }
}

function downloadReport() {
    for(let i in currentReport){
        exportToExcel(currentReport[i]);
    }
}