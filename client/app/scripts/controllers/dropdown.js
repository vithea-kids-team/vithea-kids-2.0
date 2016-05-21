'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:DropdownCtrl
 * @description
 * # DropdownCtrl
 * Controller of the clientApp
 */
angular.module('ui.bootstrap.demo')
   .controller('DropdownCtrl', function ($scope, $log) {
  $scope.items = [
    'Masculino', 
    'Feminino',
    'Outro'
  ];

  $scope.status = {
    isopen: false
  };

  $scope.toggled = function(open) {
    $log.log('Dropdown is now: ', open);
  };

  $scope.toggleDropdown = function($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.status.isopen = !$scope.status.isopen;
  };

  $scope.appendToEl = angular.element(document.querySelector('#dropdown-long-content'));
});