(function () {

    var servletModule = angular.module('ServletResultApp', []);

    servletModule.constant('ServletModuleConstants', {
        DATA_HTML: '.data.html'
    });
    
    /**
     * Controller for Servlet Module
     */
    servletModule.controller('ServletResultController', ['$scope', '$http', '$attrs', 'ServletModuleService', 'ServletModuleConstants', function ($scope, $http, $attrs, ServletModuleService, ServletModuleConstants) {

        var contentPath = document.getElementById('contentPathValueForServlet').value;
        var currentPagePath = document.getElementById('currentPagePath').value;
        
        ServletModuleService.getResult(currentPagePath, contentPath).then(function success(response) {
            $scope.resultsList = response.data;
        });
    }]);

    /**
     * Factory for Servlet Module
     */
    servletModule.factory('ServletModuleService', ['$http', 'ServletModuleConstants', function ($http, ServletModuleConstants) {
        return {
            getResult: function (currentPagePath, contentPath) {
                var url = currentPagePath + ServletModuleConstants.DATA_HTML;
                return $http({
                   url: url, 
                   method: "GET",
                   params: {
                       contentPath: contentPath
                   }
                 });
            }
        };
    }]);
})();
