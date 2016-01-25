
var menuModule = angular.module('menuModule', []);
menuModule.controller('menuCtrl', function($scope, $http, $sce) {
    var table = [];

    $http.get("menu-bonus.json").then(function(response) {

        var titles = findMenuTitle(response.data.Navigation);
        var subTitles = findSubMenuTitle(response.data.Navigation);
        var menu = [];

        for (var i = 0; i < titles.length ; i++)
        {
          menu.push({title: titles[i], subTitle: subTitles[i]});
        }

        $scope.menu = menu;
    });

    $http.get("seances.json").then(function(response) {

        $scope.tableData = response.data;

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

    $scope.trust = $sce.trustAsHtml;

    $scope.rowStyles = table;
    $scope.styles = ["title"];
    $scope.indent = [undefined,"indent"];


});

function findMenuTitle(data)
{
  var table = [];
  for (var i = 0; i < data.length ; i++)
  {
    if (data[i].Niveau === 0)
    {
      table.push(data[i]);
    }
  }
  return table;
}

function findSubMenuTitle(data)
{
  var table = [];
  var subTable = [];

  for (var i = 0; i < data.length ; i++)
  {
    if (data[i].Niveau === 0)
    {
      if (i+1 == data.length || data[i+1].Niveau === 0)
      {
        table.push(subTable.slice(0));
        subTable.splice(0,subTable.length);
      }

    }
    else 
    {
      subTable.push(data[i]);
      if (i+1 == data.length || data[i+1].Niveau === 0)
      {
        table.push(subTable.slice(0));
        subTable.splice(0,subTable.length);
      }
    }
  }

  return table;
}
