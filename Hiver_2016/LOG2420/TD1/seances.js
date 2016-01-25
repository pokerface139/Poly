
var menuModule = angular.module('menuModule', []);
menuModule.controller('menuCtrl', function($scope, $http, $sce) {
    var table = [];

    // Telechargement du menu
    $http.get("menu.json").then(function(response) {
        $scope.menuData = response.data.Navigation;
    });

    // Telechargement du tableau des seances
    $http.get("seances.json").then(function(response) {
        $scope.tableData = response.data;

        // Association dun style pour les rangees du tableau
        for (var i = 0; i < response.data.length; i++ )
        {
          if(response.data[i].transparents === "")
          {
            table.push("special-row");
          }
          else
          {
            table.push("");
          }

        }

    });

    // Fonction pour valider le code html
    $scope.trust = $sce.trustAsHtml;

    // Variables servant au style
    $scope.rowStyles = table;
    $scope.styles = ["title"];
    $scope.indent = [undefined,"indent"];



});
