'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:ListchildrenCtrl
 * @description
 * # ListchildrenCtrl
 * Controller of the clientApp
 */
angular.module('clientApp')
  .controller('ListchildrenCtrl', function ($scope, $http, $log, alertService, $location, userService) {

	  $scope.getChildren = function() {
       $http.get('app/listchildren')
           .success(function(data) {
             $scope.children = data;
           });
     };

     $scope.getChildren();
  });