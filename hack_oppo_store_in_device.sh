#! /system/bin/sh

#source ~/.bash_profile
install_times=0

while true
do
    am instrument -w -r   -e debug false -e class com.wereading.wereadinghacker.OppoAppStoreTest com.wereading.wereadinghacker.test/android.support.test.runner.AndroidJUnitRunner
    sleep 3    
    let install_times+=1
    echo "install_times = ${install_times}"
done

