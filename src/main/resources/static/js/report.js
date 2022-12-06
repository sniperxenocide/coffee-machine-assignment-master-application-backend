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
        let startDate='',endDate='';
        try {
            startDate=document.getElementById('start-date').value;
            endDate=document.getElementById('end-date').value;
        }catch (e) {}
        url+="/report/machine/summary?";
        url+=startDate.length>0?'startDate='+startDate+'&':'';
        url+=endDate.length>0?'endDate='+endDate:'';
        currentReport = ['report-table-machine-summary'];
    }
    else if(val==2) {
        let startDate='',endDate='';
        try {
            startDate=document.getElementById('start-date-2').value;
            endDate=document.getElementById('end-date-2').value;
        }catch (e) {}
        url+="/report/shop/summary/location_wise?";
        url+=startDate.length>0?'startDate='+startDate+'&':'';
        url+=endDate.length>0?'endDate='+endDate:'';
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