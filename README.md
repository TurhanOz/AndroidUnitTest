# Android Unit Test
[![Build Status](https://travis-ci.org/TurhanOz/AndroidUnitTest.svg?branch=master)](https://travis-ci.org/TurhanOz/AndroidUnitTest)

This project is intended to cover interesting Android Unit Test scenario using Robolectric 3.0, Juni4.+, Mockito 1.9.+

Each components and associated unit tests' use cases are detailed below.

## Use cases
**AlarmManager**

* setting repeated alarms triggering broadcast (and canceling previous one -> one alarm at a time)
* enable/disabling alarm on BootComplete
* reseting Alarm sequence on BootComplete
* starting a service on broadcastReceived


## Articles
Detailed process of [AlarmManager]()

License
-------

    Copyright 2015 Turhan OZ

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
