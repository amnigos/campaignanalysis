
This Hadoop MR job is used for creating campaign mobile numbers by ignoring scrubbed data. The query on MySQL was taking few hours so we were taking dump of mysql data as CSV files and using this MR job to get the required output data after ignoring scrubbed numbers.

Total job processing time is less than 5 minutes.

Job Execution Command:

$ bin/hadoop jar <path-to-mobilecountmrjob-jar> <input-path> <output-path>

<input-path> - This directory should have two csv files, One with all mobile numbers and other with scrubbed numbers.

<output-path> - This shouldn't exist else Hadoop job will fail.

