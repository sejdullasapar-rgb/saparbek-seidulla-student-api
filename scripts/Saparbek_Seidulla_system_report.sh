#!/usr/bin/env bash
# System monitoring report of Saparbek Seidulla (DevOps midterm, Task 1.2)
# Prints system information and appends it to the personal log file.

STUDENT_NAME="Saparbek Seidulla"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_FILE="$SCRIPT_DIR/../logs/Saparbek_Seidulla_system_report.log"
OS_TYPE="$(uname -s)"

{
  echo "=================================================="
  echo " System report"
  echo "=================================================="
  echo "Student full name : $STUDENT_NAME"
  echo "Date and time     : $(date '+%Y-%m-%d %H:%M:%S %Z')"
  echo "Current username  : $(whoami)"
  echo "Hostname          : $(hostname)"
  echo

  echo "--- Operating system and kernel ---"
  if [ "$OS_TYPE" = "Darwin" ]; then
    sw_vers
  else
    grep PRETTY_NAME /etc/os-release | cut -d= -f2 | tr -d '"'
  fi
  echo "Kernel            : $(uname -s) $(uname -r) ($(uname -m))"
  echo

  echo "--- CPU ---"
  if [ "$OS_TYPE" = "Darwin" ]; then
    echo "Model             : $(sysctl -n machdep.cpu.brand_string)"
    echo "Cores             : $(sysctl -n hw.ncpu)"
  else
    echo "Model             : $(grep -m1 'model name' /proc/cpuinfo | cut -d: -f2 | sed 's/^ //')"
    echo "Cores             : $(nproc)"
  fi
  echo "Load average      : $(uptime | sed 's/.*load average[s]*: //')"
  echo

  echo "--- Memory ---"
  if [ "$OS_TYPE" = "Darwin" ]; then
    echo "Total RAM         : $(( $(sysctl -n hw.memsize) / 1024 / 1024 )) MB"
    memory_pressure 2>/dev/null | tail -1
    vm_stat | head -5
  else
    free -h
  fi
  echo

  echo "--- Disk usage ---"
  df -h | grep -vE '^(map|devfs|tmpfs|overlay)'
  echo

  echo "--- Available disk space (home directory) ---"
  df -h "$HOME" | awk 'NR==2 {print "Available: " $4 " of " $2 " (" $5 " used) on " $NF}'
  echo
} | tee -a "$LOG_FILE"

echo "Report appended to $LOG_FILE"
