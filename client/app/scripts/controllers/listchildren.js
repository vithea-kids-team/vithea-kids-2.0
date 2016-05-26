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

      $scope.childrenList = [
         {firstName: 'Laurent', lastName: 'Renard', username: 'username', password: 'password', gender: 'girl'},
         {firstName: 'Blandine', lastName: 'Faivre', username: 'username', password: 'password', gender: 'girl'},
         {firstName: 'Francoise', lastName: 'Frere', username: 'username', password: 'password', gender: 'girl'}
     ];
  });