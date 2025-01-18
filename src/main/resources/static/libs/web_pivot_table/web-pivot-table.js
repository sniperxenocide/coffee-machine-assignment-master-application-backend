class Node {
    constructor(nodeValue, depth, nodeFullValue) {
        this.id = '';
        this.nodeValue = nodeValue;
        this.nodeFullValue = nodeFullValue;
        this.depth = depth;
        this.measures = [];
        this.children = [];
        this.colChildren = [];
        this.expanded = false;
        this.filterSelected = true;
        this.filterVisible = true;
    }
}

let plusIconClass = 'fa-plus-circle';
let minusIconClass = 'fa-minus-circle';

let SORT_ASC = 1;
let SORT_DESC = -1;
let SORT_NULL = 0;
let sortAscClass = 'fas fa-sort-amount-down-alt';
let sortDescClass = 'fas fa-sort-amount-down';

let columnExpanded = false;

let REPORT_DATA = null;
let TABLE_CONTAINER_ID = '';
let ROOT_NODE = null;
let NODE_MAP = {};
let ROW_WISE_NODES = {};
let COL_NODE_VAL = [];
let reportConfig = {rows: [], rowCaptions: [], columns: [], columnCaptions: [], measures: [], measureCaptions: []}

let DATA_FIELDS = [];
let NUMERIC_FIELDS = [];
let TEXT_FIELDS = [];
let NULL_FIELDS = [];

let reportConfigChangedListenerFunc = null;

class WebPivotTable {
    constructor(data, config, id, notifyOnReportConfigChange) {
        REPORT_DATA = data;
        TABLE_CONTAINER_ID = id;
        reportConfigChangedListenerFunc = notifyOnReportConfigChange;
        reportConfig = {
            rows: config.rows,
            rowCaptions: config.rowCaptions,
            columns: config.columns,
            columnCaptions: config.columnCaptions,
            measures: config.measures,
            measureSorts: [...Array(config.measures.length)].fill(SORT_NULL),
            measureCaptions: config.measureCaptions
        }

        createReport();
    }
}

function createReport(){
    reportConfig.rows.forEach(value => {
        ROW_WISE_NODES[value] = []
    });
    NODE_MAP = {};
    COL_NODE_VAL = [];
    columnExpanded = false;

    validateReportData();
    generateTree();
    sortTreeByMeasure(0);
    generateTable();

    console.log('Config ', reportConfig);
    console.log('ROOT_NODE ', ROOT_NODE);
    console.log('ROW_WISE_NODES ', ROW_WISE_NODES);
    console.log('COL_NODE_VAL ', COL_NODE_VAL);
}

function validateReportData() {
    if (REPORT_DATA == null || REPORT_DATA.length === 0) {
        console.log('No Data Found');
        return;
    }
    DATA_FIELDS = Object.keys(REPORT_DATA[0]);
    console.log('Data Fields ', DATA_FIELDS.length, ' ', DATA_FIELDS);

    NUMERIC_FIELDS = [];
    TEXT_FIELDS = [];
    NULL_FIELDS = [];

    DATA_FIELDS.forEach(k => {
        let isNumeric = false;
        let nullCnt = 0;
        let numberCnt = 0;
        let dateCnt = 0;
        for (let i = 0; i < REPORT_DATA.length; i++) {
            let val = REPORT_DATA[i][k];
            if(val==null || val=='null' || val.length==0) nullCnt++; // Checking Null
            else if(typeof val === 'number') numberCnt++;  // Checking Numeric
            else {
                // Checking date value by matching date pattern
                // dd-mm-yyyy or yyyy-mm-dd or mm-dd-yyyy
                let regYear = '2[0-9]{3}';
                let regMonth = '(1[0-2]|0[1-9])';
                let regDate = '(3[01]|[12][0-9]|0[1-9])';
                let dmy = '^'+regDate+'(-)'+regMonth+'(-)'+regYear+'.*$';
                let ymd = '^'+regYear+'(-)'+regMonth+'(-)'+regDate+'.*$';
                let mdy = '^'+regMonth+'(-)'+regDate+'(-)'+regYear+'.*$';
                if(val.match(dmy) || val.match(ymd) || val.match(mdy)) {
                    dateCnt++;
                    let dt = new Date(val);
                    let yyyy = dt.getFullYear(),
                        mon= dt.toLocaleString('en-us', { month: 'long' }),
                        dd = dt.getDate();
                    // Creating new fields as Year Month Date separated
                    REPORT_DATA[i][k+'.Year'] = yyyy;
                    REPORT_DATA[i][k+'.Month'] = mon;
                    REPORT_DATA[i][k+'.Day'] = dd;
                    REPORT_DATA[i][k+'.Date'] = yyyy+'-'+mon+'-'+dd;
                }
            }
        }
        if(numberCnt>0 && numberCnt==REPORT_DATA.length) NUMERIC_FIELDS.push(k);
        else if(nullCnt==REPORT_DATA.length) {
            NULL_FIELDS.push(k);
        }
        else if(dateCnt>0) {
            if(!TEXT_FIELDS.includes(k)) TEXT_FIELDS.push(k);
            if(!TEXT_FIELDS.includes(k+'.Year')) TEXT_FIELDS.push(k+'.Year');
            if(!TEXT_FIELDS.includes(k+'.Month')) TEXT_FIELDS.push(k+'.Month');
            if(!TEXT_FIELDS.includes(k+'.Day')) TEXT_FIELDS.push(k+'.Day');
            if(!TEXT_FIELDS.includes(k+'.Date')) TEXT_FIELDS.push(k+'.Date');

            // Adding new fields to the Field list
            if(!DATA_FIELDS.includes(k+'.Year')) DATA_FIELDS.push(k+'.Year');
            if(!DATA_FIELDS.includes(k+'.Month')) DATA_FIELDS.push(k+'.Month');
            if(!DATA_FIELDS.includes(k+'.Day')) DATA_FIELDS.push(k+'.Day');
            if(!DATA_FIELDS.includes(k+'.Date')) DATA_FIELDS.push(k+'.Date');
        }
        else TEXT_FIELDS.push(k);

    })

    // Removing NULL fields from Field List and ReportConfig
    NULL_FIELDS.forEach(v=>{
        DATA_FIELDS.splice(DATA_FIELDS.indexOf(v),1);

        let idx = reportConfig.rows.indexOf(v);
        if(idx > -1)  {
            reportConfig.rows.splice(idx,1);
            reportConfig.rowCaptions.splice(idx,1);
        }

        idx = reportConfig.columns.indexOf(v);
        if(idx > -1)  {
            reportConfig.columns.splice(idx,1);
            reportConfig.columnCaptions.splice(idx,1);
        }

        idx = reportConfig.measures.indexOf(v);
        if(idx > -1)  {
            reportConfig.measures.splice(idx,1);
            reportConfig.measureCaptions.splice(idx,1);
        }
    })

    console.log('Data Fields ', DATA_FIELDS.length, ' ', DATA_FIELDS);
    console.log('Text Fields ', TEXT_FIELDS.length, ' ', TEXT_FIELDS);
    console.log('Numeric Fields ', NUMERIC_FIELDS.length, ' ', NUMERIC_FIELDS);
    console.log('NULL Fields ', NULL_FIELDS.length, ' ', NULL_FIELDS);

}

function generateTree() {
    ROOT_NODE = new Node("Parent", 0, '');
    let idCnt = 1;
    for (let i in REPORT_DATA) {
        let currentNode = ROOT_NODE;
        let data = reportData[i];
        reportConfig.rows.forEach(value => {
            let childFound = false;
            for (let ci in currentNode.children) {
                let child = currentNode.children[ci];
                if (child.nodeValue == data[value]) {
                    currentNode = child;
                    childFound = true;

                    // // calculating measure
                    reportConfig.measures.forEach((value, index) => {
                        child.measures[index] = child.measures[index] + data[value];
                    });

                    generateColumnTree(currentNode, data);

                    break;
                }
            }
            if (childFound == false) {
                let fullVal = (currentNode.nodeFullValue ==null || currentNode.nodeFullValue.length == 0)
                    ? data[value] : currentNode.nodeFullValue + '.' + data[value];
                let child = new Node(data[value], currentNode.depth + 1, fullVal);

                // calculating measure
                reportConfig.measures.forEach(value => {
                    child.measures.push(data[value]);
                })

                currentNode.children.push(child);
                child.id = 'node-' + idCnt++;
                currentNode = child;
                NODE_MAP[child.id] = child;
                ROW_WISE_NODES[value].push(child);

                generateColumnTree(currentNode, data);
            }
        })
    }
    COL_NODE_VAL.sort();
}

function generateColumnTree(node, data) {
    let currentNode = node;
    let fullVal = '';
    reportConfig.columns.forEach(value => {
        let childFound = false;
        for (let ci in currentNode.colChildren) {
            let child = currentNode.colChildren[ci];
            if (child.nodeValue == data[value]) {
                currentNode = child;
                childFound = true;

                // calculating measure
                reportConfig.measures.forEach((value, index) => {
                    child.measures[index] = child.measures[index] + data[value];
                });

                break;
            }
        }
        if (childFound == false) {
            fullVal = fullVal.length == 0 ? data[value] : currentNode.nodeFullValue + '.' + data[value];
            let child = new Node(data[value], currentNode.depth + 1, fullVal);

            // calculating measure
            reportConfig.measures.forEach(value => {
                child.measures.push(data[value]);
            })

            currentNode.colChildren.push(child);
            child.id = node.id + '.' + currentNode.colChildren.length;
            currentNode = child;
            NODE_MAP[child.id] = child;

            if (node.depth == 1 && child.depth == reportConfig.columns.length + 1) {
                if (!COL_NODE_VAL.includes(child.nodeFullValue)) COL_NODE_VAL.push(child.nodeFullValue);
            }
        }
    })
}

function calculateMeasureValues() {
    ROOT_NODE.children.forEach(n => getMeasureFromChild(n));

    function getMeasureFromChild(child) {
        let M = [];
        reportConfig.measures.forEach(() => {
            M.push(0)
        });
        let MM = [];
        COL_NODE_VAL.forEach(() => {
            let L = [];
            reportConfig.measures.forEach(() => {
                L.push(0)
            });
            MM.push(L);
        })
        if (!child.filterSelected) return {measures: M, colMeasures: MM};

        let leafColNodes = [];
        getColumnLeafNodes(child, leafColNodes);
        leafColNodes.sort((a, b) => {
            return (a.nodeFullValue > b.nodeFullValue ? 1 : -1);
        });

        if (child.children.length == 0) {
            M = child.measures;

            for (let i = 0, j = 0; i < COL_NODE_VAL.length; i++) {
                if (j < leafColNodes.length && COL_NODE_VAL[i] == leafColNodes[j].nodeFullValue) {
                    MM[i] = leafColNodes[j].measures;
                    j++;
                }
            }
        }
        child.children.forEach(n => {
            let CM = getMeasureFromChild(n);
            for (let i = 0; i < M.length; i++) M[i] += CM.measures[i];

            for (let i = 0; i < MM.length; i++) {
                for (let j = 0; j < MM[i].length; j++) {
                    MM[i][j] += CM.colMeasures[i][j];
                }
            }
        });

        child.measures = M;

        for (let i = 0, j = 0; i < COL_NODE_VAL.length; i++) {
            if (j < leafColNodes.length && COL_NODE_VAL[i] == leafColNodes[j].nodeFullValue) {
                leafColNodes[j].measures = MM[i];
                j++;
            }
        }

        return {measures: M, colMeasures: MM};
    }
}

function generateTable() {
    $('#' + TABLE_CONTAINER_ID).html(fieldSelectionModal
        + filterModal + webPivotTableHtml);
    generateTableHeader();
    generateTableBody();
}

function generateTableHeader() {
    let col1 = '';
    reportConfig.rowCaptions.forEach((value, index, A) => {
        let header = reportConfig.rows[index];
        let key = '\'' + header + '\'';
        let title = '\'' + value + '\'';
        let headerBtn = '<button onclick="openFilterModal(' + key + ',' + title + ')" class="btn btn-sm btn-secondary px-2 m-1" style="background:#657e72"> <b>' + value + '</b> <i id="' + header + '-filter-header-icon" style="color: white" class="fa fa-cog px-1"></i></button>';
        let separatingComma = (index < A.length - 1 ? '<label style="display: none">,</label>' : ''); // For Excel Export Purpose only
        col1 += headerBtn + separatingComma;
    });
    let firstColFreezeStyle = 'position: sticky;left: 0;z-index: 3;';
    let tr = '<th class="text-start align-middle" style="min-width: 200px;max-width: 300px;' + firstColFreezeStyle + '" rowspan="2">'
        + col1 + '</th>';

    // For Excel Export Purpose only
    // for(let x=0;x<reportConfig.rows.length-1;x++) tr+='<th rowspan="2" style="display: none"></th>';

    let col2 = '';
    reportConfig.columnCaptions.forEach((value, index, A) => {
        let headerBtn = '<button class="btn btn-sm btn-secondary px-4 m-1" style="background:#657e72"> <b>' + value + '</b></button>';
        let separatingComma = (index < A.length - 1 ? '<label style="display: none">,</label>' : ''); // For Excel Export Purpose only
        col2 += headerBtn + separatingComma;
    });

    col2 += '<button onclick="columnExpandControl()" class="btn btn-light px-2 ms-4"> <i class="fa ' + (columnExpanded ? minusIconClass : plusIconClass) + ' px-1" style="color: ' + (columnExpanded ? 'red' : 'green') + '"></i></button>';
    tr += '<th class="text-end align-middle text-nowrap" style="min-width: 300px" colspan="' + reportConfig.measures.length + '">' + col2 + '</th>';

    if (columnExpanded) COL_NODE_VAL.forEach(value => {
        tr += '<th colspan="' + reportConfig.measures.length + '" class="text-center align-middle text-nowrap">' + value.replace('.', ' > ') + '</th>'
    });
    tr = '<tr>' + tr + '</tr>';

    reportConfig.measureCaptions.forEach((value, index) => {
        let sortIconClass = '';
        if (reportConfig.measureSorts[index] == SORT_ASC) sortIconClass = sortAscClass;
        else if (reportConfig.measureSorts[index] == SORT_DESC) sortIconClass = sortDescClass;
        tr +=
            '<th class="text-center align-middle text-nowrap" onclick="sortTableByMeasure(' + index + ')">' +
            '<label>' + value + '</label>' +
            '<i class="' + sortIconClass + ' px-2"></i>' +
            '</th>';
    })

    if (columnExpanded) {
        COL_NODE_VAL.forEach(() => {
            reportConfig.measureCaptions.forEach(v => {
                tr += '<th class="text-center align-middle text-nowrap">' + v + '</th>';
            });
        });
    }
    tr = '<tr>' + tr + '</tr>';

    $('#pivot-table-head').html(tr);
}

function generateTableBody() {
    $('#pivot-table-body').html(insertTableRow(ROOT_NODE.children,true));
}

function insertTableRow(nodeChildren,grandTotalNeeded) {
    let grandTotal = [];
    for(let i=0;i<(COL_NODE_VAL.length+1)*reportConfig.measures.length;i++) grandTotal[i]=0
    let grandTotalTr = '<tr style="position: sticky;bottom: 0;z-index: 2;"><td class="text-nowrap d-flex align-items-center" style="position: sticky;left: 0;z-index: 1;background:#f7ffff;"><label style="font-weight: bold">Grand Total</label></td>';

    let tbody = '';
    nodeChildren.forEach((node,idx) => {
        if (!node.filterSelected) return;
        let iconElem = node.expanded ?
            '<i id="' + node.id + '-icon" class="me-1 fa ' + minusIconClass + '" style="color: red"></i>' :
            '<i id="' + node.id + '-icon" class="me-1 fa ' + plusIconClass + '" style="color: green"></i>';
        let icon = node.children.length > 0 ? iconElem : '';
        let firstColFreezeStyle = 'style="position: sticky;left: 0;z-index: 1;background:#f7ffff;"';
        let space = '&nbsp'.repeat((node.depth - 1) * 4);
        let startingFakeTd = '<td style="display: none"></td>'.repeat(node.depth - 1); // For Excel Export Purpose only
        let endingFakeTd = '<td style="display: none"></td>'.repeat(reportConfig.rows.length - node.depth); // For Excel Export Purpose only
        let tr = '<td class="text-nowrap d-flex align-items-center" onclick="expandCollapseControl(\'' + node.id + '\')" ' + firstColFreezeStyle + '>'
            + space + icon + '<label>'+(idx+1)+'.&nbsp;</label><label>'+node.nodeValue + '</label></td>';
        // tr = startingFakeTd+tr+endingFakeTd; // For Excel Export Purpose only
        node.measures.forEach((value,idx) => {
            tr += '<td class="text-end align-middle">' + value.toLocaleString() + '</td>';
            grandTotal[idx]+=value;
        }); //.toFixed(2)

        // Adding measures for columns
        if (columnExpanded) {
            let leafColNodes = [];
            getColumnLeafNodes(node, leafColNodes);
            leafColNodes.sort((a, b) => {
                return (a.nodeFullValue > b.nodeFullValue ? 1 : -1);
            });
            for (let i = 0, j = 0; i < COL_NODE_VAL.length; i++) {
                if (j < leafColNodes.length && COL_NODE_VAL[i] == leafColNodes[j].nodeFullValue) {
                    leafColNodes[j].measures.forEach((value,idx) => {
                        tr += '<td class="text-end align-middle">' + value.toLocaleString() + '</td>';
                        grandTotal[idx+reportConfig.measures.length*(i+1)]+=value;
                    });
                    j++;
                } else reportConfig.measures.forEach(() => {
                    tr += '<td>--</td>';
                });
            }
        }

        tr = '<tr id="' + node.id + '" >' + tr + '</tr>';
        tbody = tbody + tr + (node.expanded ? insertTableRow(node.children) : '');
    })
    if(grandTotalNeeded){
        grandTotal.forEach((val,idx)=>{
            if(!columnExpanded && idx>=reportConfig.measures.length) return;
            grandTotalTr+='<td class="text-end align-middle" style="font-weight: bold">'+val.toLocaleString()+'</td>';
        });
        grandTotalTr+='</tr>';
        tbody+=grandTotalTr;
    }
    return tbody;
}

function getColumnLeafNodes(colNode, leaf) {
    if (colNode.colChildren.length == 0) leaf.push(colNode);
    colNode.colChildren.forEach(c => {
        getColumnLeafNodes(c, leaf);
    })
}

function expandCollapseControl(elemId) {
    let node = NODE_MAP[elemId];
    if (node.expanded == false) expandRow(node, false);
    else collapseRow(node);
}

function expandRow(node, expandAll) {
    node.expanded = true;
    $('#' + node.id + '-icon').addClass(minusIconClass)
        .removeClass(plusIconClass).css('color', 'red');
    $('#' + node.id).after(insertTableRow(node.children));

    if (expandAll) node.children.forEach(c => expandRow(c, true));
}

function collapseRow(node) {
    node.expanded = false;
    $('#' + node.id + '-icon').addClass(plusIconClass)
        .removeClass(minusIconClass).css('color', 'green');
    removeChildRow(node);
}

function expandAll() {
    for (let k in NODE_MAP) NODE_MAP[k].expanded = false;
    generateTable();
    ROOT_NODE.children.forEach(c => expandRow(c, true));
}

function collapseAll() {
    for (let k in NODE_MAP) NODE_MAP[k].expanded = false;
    generateTable();
}

function columnExpandControl() {
    columnExpanded = !columnExpanded;
    generateTable();
}

function removeChildRow(node) {
    node.children.forEach(child => {
        $('#' + child.id).remove();
        removeChildRow(child);
    })
}

let nameWiseFilterNodes = {};
let currentFilterKey = null;
let filterItemIdPrefix = '';
function openFilterModal(key, title) {
    nameWiseFilterNodes = {};
    currentFilterKey = key;
    filterItemIdPrefix = 'filter-item-'+currentFilterKey+'-';
    $('#filter-item-search').val('');
    $('#sort-asc').addClass('btn-outline-success').removeClass('btn-success');
    $('#sort-desc').addClass('btn-outline-success').removeClass('btn-success');

    ROW_WISE_NODES[key].forEach(node => {
        if(!node.filterVisible) return;

        if(!(node.nodeValue in nameWiseFilterNodes))
            nameWiseFilterNodes[node.nodeValue] = {
                    selected:false,
                    visible:true,
                    nodes:[]
                };

        nameWiseFilterNodes[node.nodeValue]
            .nodes.push({
                selected:node.filterSelected,
                node:node
            });

        if(!(nameWiseFilterNodes[node.nodeValue].selected))
            nameWiseFilterNodes[node.nodeValue].selected = node.filterSelected;
    });
    generateFilterPage();
    $('#filter-modal-title').html('Filter ' + title);
    $('#filter-modal').modal('show');
}

function generateFilterPage() {
    generateFilterChecklist(1);
    generateFilterMeasureList();
}

function generateFilterChecklist(sortOrder) {
    let checklist = '';
    let keys = Object.keys(nameWiseFilterNodes);
    keys.sort((a,b) => {return (a>b?1:-1) * sortOrder;});
    filterSortIconUpdate(sortOrder);

    let sl = 1;
    keys.forEach((name)=>{
        let item = nameWiseFilterNodes[name];
        let checked = item.selected ? 'checked="true"' : '';
        let visible = item.visible ? 'display:block' : 'display:none';
        checklist += '<li class="list-group-item" style="' + visible + '">' +
            '<input id="'+filterItemIdPrefix + name +'" ' +
            'onclick="filterItemCheckEvent(\''+name+'\',this)" class="form-check-input me-1" ' +
            'type="checkbox" ' + checked + '/> '+sl+'. ' + name + '</li>';
        if(item.visible) sl++;
    });
    checklist = '<ul class="list-group list-group-light">' + checklist + '</ul>';
    $('#filter-modal-body-l').html(checklist);

    updateFilterItemSelectedCount();
}

function generateFilterMeasureList() {
    let measures = '';
    reportConfig.measureCaptions.forEach((v,idx)=>{
        measures +=
            '<tr><td colspan="2">'+v+'</td></tr>'+
            '<tr>'+
            '<td><input id="filter-measure-min-'+idx+'" oninput="filterWithSearchAndMeasureVal()" type="number" placeholder="Min" class="form-control form-control-sm"></td>'+
            '<td><input id="filter-measure-max-'+idx+'" oninput="filterWithSearchAndMeasureVal()" type="number" placeholder="Max" class="form-control form-control-sm"></td>'+
            '</tr>';
    })
    $('#filter-measure-table').html(measures);
}

function filterSelectAll(checkBoxStatus) {
    for(let name in nameWiseFilterNodes){
        let itemId = filterItemIdPrefix+name;
        if (nameWiseFilterNodes[name].visible) {
            nameWiseFilterNodes[name].selected = checkBoxStatus;
            document.getElementById(itemId).checked = checkBoxStatus;
            nameWiseFilterNodes[name].nodes.forEach(n=>{n.selected = checkBoxStatus;})
        } else {
            nameWiseFilterNodes[name].selected = false;
            document.getElementById(itemId).checked = false;
            nameWiseFilterNodes[name].nodes.forEach(n=>{n.selected = false;})
        }
    }
    updateFilterItemSelectedCount();
}

function filterItemCheckEvent(name, e) {
    nameWiseFilterNodes[name].selected = e.checked;
    nameWiseFilterNodes[name].nodes.forEach(n=>{n.selected = e.checked;})
    updateFilterItemSelectedCount();
}

function filterWithSearchAndMeasureVal() {
    for(let name in nameWiseFilterNodes){
        let condition = true;

        let searchKey = $('#filter-item-search').val();
        if(searchKey==null || searchKey.length===0) condition = true;
        else condition = name.toLowerCase().includes(searchKey.toLowerCase());

        nameWiseFilterNodes[name].nodes.forEach(n=>{
            let node = n.node;
            let measureCondition = true;
            for(let idx=0;idx<reportConfig.measures.length;idx++){
                let minVal = $('#filter-measure-min-'+idx).val();
                let maxVal = $('#filter-measure-max-'+idx).val();

                if(minVal==null || minVal.length===0) measureCondition = true;
                else measureCondition = node.measures[idx] >= minVal;
                if(!measureCondition) break;

                if(maxVal==null || maxVal.length===0) measureCondition = true;
                else measureCondition = node.measures[idx] <= maxVal;
                if(!measureCondition) break;
            }
            n.selected = condition && measureCondition;
            condition = condition && n.selected;
        });

        nameWiseFilterNodes[name].visible = condition;
        nameWiseFilterNodes[name].selected = condition;
    }

    generateFilterChecklist(1);
}

function filterSortIconUpdate(sortOrder) {
    if (sortOrder == 1) {
        $('#sort-asc').addClass('btn-success').removeClass('btn-outline-success');
        $('#sort-desc').addClass('btn-outline-success').removeClass('btn-success');
    } else {
        $('#sort-asc').addClass('btn-outline-success').removeClass('btn-success');
        $('#sort-desc').addClass('btn-success').removeClass('btn-outline-success');
    }
}

function updateFilterItemSelectedCount() {
    let selectedCnt = 0;
    for(let name in nameWiseFilterNodes){
        if (nameWiseFilterNodes[name].selected) selectedCnt++;
    }
    $('#filter-item-select-count').html('&nbsp( ' + selectedCnt + ' of ' + Object.keys(nameWiseFilterNodes).length + ' selected )');

    console.log('Filter Object ',nameWiseFilterNodes);
}

function applyFilter() {
    let selectedCnt = 0;

    for(let name in nameWiseFilterNodes){
        nameWiseFilterNodes[name].nodes.forEach(n=>{
            n.node.filterSelected = n.selected;
            filterDisablingChildNodes(n.node.children,n.selected);
        });
        if(nameWiseFilterNodes[name].selected) selectedCnt++;
    }

    let filterIconColor = selectedCnt < Object.keys(nameWiseFilterNodes).length ? 'orange' : 'white';
    $('#' + currentFilterKey + '-filter-header-icon').css('color', filterIconColor);

    $('#filter-modal').modal('hide');
    calculateMeasureValues();
    generateTableBody();
}

function filterDisablingChildNodes(children,isSelected) {
    children.forEach(n=>{
        n.filterSelected = isSelected;
        n.filterVisible = isSelected;
        filterDisablingChildNodes(n.children,isSelected);
    })
}

function sortTableByMeasure(measureIdx) {
    sortTreeByMeasure(measureIdx);
    generateTable();
}

function sortTreeByMeasure(measureIdx) {
    if (reportConfig.measureSorts[measureIdx] == SORT_NULL) {
        reportConfig.measureSorts[measureIdx] = SORT_DESC;
    } else reportConfig.measureSorts[measureIdx] = reportConfig.measureSorts[measureIdx] * (-1);
    for (let i = 0; i < reportConfig.measureSorts.length; i++) {
        if (i != measureIdx) reportConfig.measureSorts[i] = SORT_NULL;
    }
    sortNode(ROOT_NODE);

    function sortNode(node) {
        let sortOrder = reportConfig.measureSorts[measureIdx];
        node.children.sort((a, b) => {
            return (a.measures[measureIdx] - b.measures[measureIdx]) * sortOrder;
        });
        node.children.forEach(n => sortNode(n));
    }
}

function printTablePdf() {
    window.jsPDF = window.jspdf.jsPDF;
    const pdf = new jsPDF({orientation:'l'});

    pdf.autoTable({
        html: '#pivot-table' ,
        theme:'grid',
        styles: {textColor: '#000000'},
        headStyles:{
            fillColor:'#47474a',
            textColor: '#FFFFFF',
            fontStyle: 'bold',
            halign: 'center',
            valign: 'middle',
            lineWidth: 0.4
        },
        bodyStyles: {
            valign: 'middle',
            halign: 'right',
            cellWidth : 'wrap',
            overflow:'visible'
        },
        columnStyles: {
            0:{
                halign: 'left',
                cellWidth : 'auto',
                overflow:'linebreak'
            }
        }
    });
    pdf.save('Report.pdf');
}

function exportToExcel() {
    let table = document.getElementById('pivot-table');
    TableToExcel.convert(table,{name: 'Report.xlsx', sheet: {name: 'Report'}});

    // let uri = 'data:application/vnd.ms-excel;base64,'
    //     ,
    //     template = '<html lang="en" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
    //     , base64 = function (s) {
    //         return window.btoa(unescape(encodeURIComponent(s)))
    //     }
    //     , format = function (s, c) {
    //         return s.replace(/{(\w+)}/g, function (m, p) {
    //             return c[p];
    //         })
    //     }
    //
    // let table = document.getElementById('pivot-table');
    // let ctx = {worksheet: 'Report' || 'Worksheet', table: table.innerHTML}
    // window.location.href = uri + base64(format(template, ctx))
}

function exportToCsv() {
    const dataType = 'data:application/csv,';
    const tableSelect = document.getElementById('pivot-table');
    let myData = tableSelect.rows;
    let my_list = [];
    for (let i = 0; i < myData.length; i++) {
        let el = myData[i].children;
        let my_el = [];
        for (let j = 0; j < el.length; j++) {
            my_el.push(el[j].innerText.replace(',', ' '));
        }
        my_list.push(my_el);
    }

    let CsvString = "";
    my_list.forEach(r => {
        r.forEach(c => {
            CsvString += c + ',';
        });
        CsvString += "\r\n";
    });

    CsvString = CsvString.replace(/\u00a0/g, "  ");

    CsvString = encodeURIComponent(CsvString);

    let filename = 'report.csv';

    let downloadLink = document.getElementById('excel-download-link');

    document.body.appendChild(downloadLink);

    downloadLink.href = dataType + CsvString;
    downloadLink.download = filename;
    downloadLink.click();
}

let fieldConfig = null;

function openFieldSelectionModal() {
    fieldConfig = {
        ROW: {FIELDS: [], CAPTIONS: [], MAX:10},
        COLUMN: {FIELDS: [], CAPTIONS: [], MAX:2},
        MEASURE: {FIELDS: [], CAPTIONS: []},
        UNSELECTED: {FIELDS: [], CAPTIONS: []}
    };
    reportConfig.rows.forEach(v => fieldConfig.ROW.FIELDS.push(v));
    reportConfig.rowCaptions.forEach(v => fieldConfig.ROW.CAPTIONS.push(v));
    reportConfig.columns.forEach(v => fieldConfig.COLUMN.FIELDS.push(v));
    reportConfig.columnCaptions.forEach(v => fieldConfig.COLUMN.CAPTIONS.push(v));
    reportConfig.measures.forEach(v => fieldConfig.MEASURE.FIELDS.push(v));
    reportConfig.measureCaptions.forEach(v => fieldConfig.MEASURE.CAPTIONS.push(v));
    DATA_FIELDS.forEach(val => {
        if (!(fieldConfig.ROW.FIELDS.includes(val) || fieldConfig.COLUMN.FIELDS.includes(val) || fieldConfig.MEASURE.FIELDS.includes(val))) {
            fieldConfig.UNSELECTED.FIELDS.push(val);
            fieldConfig.UNSELECTED.CAPTIONS.push(val);
        }
    });
    fieldConfig.UNSELECTED.FIELDS.sort();
    fieldConfig.UNSELECTED.CAPTIONS.sort();

    buildFieldSelectionTable();
    $('#field-selection-modal').modal('show');
}

function buildFieldSelectionTable() {
    let tr = '';
    for (let key in fieldConfig) {
        let maxText = ('MAX' in fieldConfig[key]) ? ' ( MAX: '+ fieldConfig[key]['MAX']+' )' : '';
        tr += '<tr><td colspan="5"><h6>' + key +maxText+ '</h6></td></tr>';
        fieldConfig[key].FIELDS.forEach((val, idx,A) => {
            let arrow = '';
            if(key!='UNSELECTED'){
                arrow += idx>0? '<button type="button" onclick="fieldPositionMove(\''+key+'\','+idx+',-1)" class="btn btn-outline-success btn-sm m-1"><i  class="fa fa-angle-up"></i></button>':'';
                arrow += (idx<A.length-1) ? '<button type="button" onclick="fieldPositionMove(\''+key+'\','+idx+',1)" class="btn btn-outline-danger btn-sm"><i  class="fa fa-angle-down"></i></button>':'';
            }
            arrow = '<td class="text-center">'+arrow+'</td>';

            tr += '<tr class="align-middle"><td>' + (idx + 1) + '</td>'+arrow+'<td>' + val + '</td>';
            tr += '<td><input type="text" value="' + fieldConfig[key].CAPTIONS[idx] + '" onchange="fieldCaptionChange(\'' + key + '\',this.value,' + idx + ')" class="form-control"/></td>';
            tr += '<td>' + buildSelect(key, idx) + '</td></tr>';
        });
    }
    $('#field-selection-table-body').html(tr);

    function buildSelect(currentKey, idx) {
        let select = '<select onchange="fieldTypeSelect(\'' + currentKey + '\',this.value,' + idx + ')" class="form-select">';
        for (let key in fieldConfig) {
            if(key=='MEASURE' && TEXT_FIELDS.includes(fieldConfig[currentKey].FIELDS[idx])) continue;
            if('MAX' in fieldConfig[key] && fieldConfig[key].FIELDS.length>=fieldConfig[key]['MAX']) continue;
            let selected = key == currentKey ? 'selected' : '';
            select += '<option value="' + key + '" ' + selected + '>' + key + '</option>';
        }
        select += '</select>';
        return select;
    }
}

function fieldTypeSelect(currentKey, newKey, idx,) {
    let field = fieldConfig[currentKey].FIELDS[idx];
    let caption = fieldConfig[currentKey].CAPTIONS[idx];

    fieldConfig[currentKey].FIELDS.splice(idx, 1); //fieldConfig[currentKey].FIELDS =
    fieldConfig[currentKey].CAPTIONS.splice(idx, 1); //fieldConfig[currentKey].CAPTIONS =

    fieldConfig[newKey].FIELDS.push(field);
    fieldConfig[newKey].CAPTIONS.push(caption);

    buildFieldSelectionTable();
}

function fieldCaptionChange(key, value, idx) {
    let errorMsg = 'Caption for field ' + fieldConfig[key].FIELDS[idx] + ' Can not be empty!!!';
    if (value == null || value.length === 0) fieldSelectionErrorMsg(errorMsg);
    fieldSelectionErrorMsg('');
    fieldConfig[key].CAPTIONS[idx] = value;
}

function fieldPositionMove(key,idx,dir){
    let newIdx = idx+dir;
    let tmpField = fieldConfig[key].FIELDS[newIdx];
    let tmpCaption = fieldConfig[key].CAPTIONS[newIdx];

    fieldConfig[key].FIELDS[newIdx] = fieldConfig[key].FIELDS[idx];
    fieldConfig[key].CAPTIONS[newIdx] = fieldConfig[key].CAPTIONS[idx];

    fieldConfig[key].FIELDS[idx] = tmpField;
    fieldConfig[key].CAPTIONS[idx] = tmpCaption;

    buildFieldSelectionTable();
}

function fieldSelectionErrorMsg(msg) {
    $('#field-selection-error').html(msg);
}

function applyFieldSelection() {
    $('#field-selection-modal').modal('hide');

    reportConfig.rows = fieldConfig.ROW.FIELDS;
    reportConfig.rowCaptions = fieldConfig.ROW.CAPTIONS;

    reportConfig.columns = fieldConfig.COLUMN.FIELDS;
    reportConfig.columnCaptions = fieldConfig.COLUMN.CAPTIONS;

    reportConfig.measures = fieldConfig.MEASURE.FIELDS;
    reportConfig.measureCaptions = fieldConfig.MEASURE.CAPTIONS;

    reportConfig.measureSorts = [...Array(reportConfig.measures.length)].fill(SORT_NULL);

    createReport();

    reportConfigChangedListenerFunc(reportConfig);
}

function showLoadingAnimation() {
    $('#report-loading-animation').css('display','block');
    $('#pivot-table').css('display','none');
}

function hideLoadingAnimation() {
    $('#report-loading-animation').css('display','none');
    $('#pivot-table').css('display','block');
}

let reportLoadingAnimation =
    '               <div id="report-loading-animation" class="d-flex justify-content-center" style="display: none">' +
    '                   <div class="spinner-grow text-danger my-10" style="width: 20rem; height: 20rem;" role="status">\n' +
    '                       <span class="visually-hidden">Loading...</span>' +
    '                   </div>'+
    '               </div>' ;

let fieldSelectionModal =
    '<div class="modal" id="field-selection-modal" tabindex="-1" role="dialog">' +
    '        <div class="modal-dialog modal-fullscreen modal-dialog-scrollable" role="document">' +
    '            <div class="modal-content">' +
    '               <div class="modal-header">' +
    '                   <div class="container">' +
    '                   <div class="row">' +
    '                       <div class="col-md-6">' +
    '                           <h5>Field Selection</h5>' +
    '                       </div>'+
    '                       <div class="col-md-6 text-end pt-2">' +
    '                           <button type="button" class="btn btn-danger me-4" data-dismiss="modal" aria-label="Close"' +
    '                               onclick="$(\'#field-selection-modal\').modal(\'hide\');">Close</button>' +
    '                           <button type="button" onclick="applyFieldSelection()" class="btn btn-primary">Apply</button>' +
    '                       </div>'+
    '                   </div>'+
    '                   </div>'+
    '               </div>' +
    '               <div id="field-selection-modal-body" class="modal-body">' +
    '                   <table class="table table-bordered table-striped" >' +
    '                       <thead class="table-dark text-center align-middle" style="position: sticky;top: 0;z-index: 3;"><th>SL</th><th></th><th>Field</th><th>Caption</th><th>Type</th></thead>' +
    '                       <tbody id="field-selection-table-body"></tbody>' +
    '                   </table>' +
    '               </div>' +
    '               <div class="modal-footer"></div>' +
    '            </div>' +
    '        </div>' +
    '</div>';

let filterModal =
    '<div class="modal" id="filter-modal" tabindex="-1" role="dialog">' +
    '        <div class="modal-dialog modal-fullscreen modal-dialog-scrollable" role="document">' +
    '            <div class="modal-content">' +
    '               <div class="modal-header">' +
    '                   <div class="container">' +
    '                    <div class="row"><h5 id="filter-modal-title" class="modal-title text-center"></h5></div>' +
    '                    <div class="row">' +
    '                       <div class="col-md-8">' +
            '                    <div class="row pt-2">' +
            '                         <div class="col-auto px-2"><button type="button" id="sort-asc" onclick="generateFilterChecklist(1)" class="btn btn-outline-success btn-sm">A-Z</button></div>' +
            '                         <div class="col-auto px-2"><button type="button" id="sort-desc" onclick="generateFilterChecklist(-1)" class="btn btn-outline-success btn-sm">Z-A</button></div>' +
            '                         <div class="col-auto px-2">' +
            '                             <div class="input-group">' +
            '                                 <input id="filter-item-search" type="text" oninput="filterWithSearchAndMeasureVal()" class="form-control form-control-sm"' +
            '                                   placeholder="Search" aria-label="Search" aria-describedby="basic-addon2"/>' +
            '                                 <div class="input-group-append">' +
            '                                   <button class="btn btn-light btn-sm" type="button">' +
            '                                   <i class="fa fa-search px-2"></i></button>' +
            '                                 </div>' +
            '                             </div>' +
            '                         </div>' +
            '                    </div>' +
            '                    <div class="row pt-2">' +
            '                       <li class="list-group-item px-2">' +
            '                           <input class="form-check-input me-1" onclick="filterSelectAll(this.checked)" type="checkbox" checked value="" aria-label="..." />' +
            '                           Select All <label id="filter-item-select-count"></label></li>' +
            '                    </div>' +
    '                       </div>'+
    '                       <div class="col-md-4 align-self-center text-end pt-2">' +
            '                    <button type="button" class="btn btn-danger me-4" data-dismiss="modal" aria-label="Close"' +
            '                            onclick="$(\'#filter-modal\').modal(\'hide\');">' +
            '                        Close</button>' +
            '                        <button type="button" onclick="applyFilter()" class="btn btn-primary">Apply</button>' +
    '                       </div>'+
    '                    </div>'+
    '                   </div>' +
    '                </div>' +
    '               <div id="filter-modal-body" class="modal-body">' +
    '                   <div class="row">' +
    '                       <div id="filter-modal-body-l" class="col-6"></div>'+
    '                       <div id="filter-modal-body-r" class="col-6">' +
    '                           <div class="table-responsive">'+
    '                           <table class="table table-bordered table-striped" >' +
    '                               <thead class="text-center"><th colspan="2">Filter Measure</th></thead>' +
    '                               <tbody id="filter-measure-table"></tbody>' +
    '                           </table>' +
    '                           </div>'+
    '                       </div>'+
    '                   </div>'+
    '               </div>' +
    '               <div class="modal-footer"></div>' +
    '            </div>' +
    '        </div>' +
    '</div>';

let webPivotTableHtml =
    '<div class="col-sm-auto m-2">' +
    '<div class="row justify-content-center ms-1">\n' +
    '    <div class="col-auto p-2">\n' +
    '        <button onclick="openFieldSelectionModal()" class="btn btn-sm btn-dark px-2">\n' +
    '        <i class="fa fa-list px-2" ></i>Fields</button>\n' +
    '    </div>\n' +
    '    <div class="col-auto p-2">\n' +
    '        <button onclick="printTablePdf()" class="btn btn-sm btn-dark px-2">\n' +
    '        <i class="fa fa-file-pdf px-2" ></i>PDF</button>\n' +
    '    </div>\n' +
    '    <div class="col-auto p-2">' +
    '        <a id="excel-download-link"></a>' +
    '        <button onclick="exportToExcel()" class="btn btn-sm btn-dark px-2">' +
    '           <i class="fa fa-file-excel px-2" ></i>Excel</button>' +
    '    </div>\n' +
    '    <div class="col-auto p-2">\n' +
    '        <button onclick="expandAll()"  class="btn btn-sm btn-dark px-2">\n' +
    '            <i class="fa fa-expand-arrows-alt px-2" ></i>Expand</button>\n' +
    '    </div>\n' +
    '    <div class="col-auto p-2">\n' +
    '        <button onclick="collapseAll()"  class="btn btn-sm btn-dark px-2">\n' +
    '            <i class="fa fa-compress-arrows-alt px-2" ></i>Collapse</button>\n' +
    '    </div>\n' +
    '</div>' + // reportLoadingAnimation +
    '<div class="table-responsive" style="max-height: '+(screen.height*0.9)+'px;" >' +   // 600px //style="width: '+(screen.width*0.9)+'px; overflow-x: auto;"
    '<table id="pivot-table" class="table table-bordered table-striped" >' +
    '   <thead id="pivot-table-head"  class="table-dark" style="position: sticky;top: 0;z-index: 2;"></thead>' +
    '   <tbody id="pivot-table-body"></tbody>\n' +
    '</table>' +
    '</div>' +
    '</div>';

