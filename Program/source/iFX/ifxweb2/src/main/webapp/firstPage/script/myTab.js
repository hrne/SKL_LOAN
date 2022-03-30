var ifxMyTab = (function ($) {
    var $tabBtns,
        $tabPanels,
        fns;
    function init(tabBtnCls, tabPanelCls, fnCallbacks) {
        $tabBtns = $("." + tabBtnCls).addClass("aim").on('click', handleBtnClick);
        $tabPanels = $("." + tabPanelCls).hide();
        fns = fnCallbacks;
        $tabBtns.eq(0).click();
    }
    function handleBtnClick() {
        var $thisBtn = $(this);
        var n = $tabBtns.index($thisBtn);
        selectTab(n)
        setTimeout(fns[n], 1);
    }
    function selectTab(n) {
        $tabPanels.hide();
        $tabPanels.eq(n).show();
        $tabBtns.removeClass("ain");
        $tabBtns.eq(n).addClass("ain");
    }

    return {
        init: init
    }
})(jQuery);