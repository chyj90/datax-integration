export function timeFix () {
    const time = new Date()
    const hour = time.getHours()
    return hour < 9 ? '早上好' : hour <= 11 ? '上午好' : hour <= 13 ? '中午好' : hour < 20 ? '下午好' : '晚上好'
  }

const metricMap = {
  'jvm.gc.pause': 'GC总耗时/s',
  'jvm.memory.used': 'JVM 已用内存/M',
  'jvm.memory.committed': 'JVM可用内存/M',
  'jvm.threads.live': '线程总数',
  'process.cpu.usage': '进程CPU',
  'process.uptime': 'UPTIME/s',
  'system.cpu.usage': '系统CPU',
  'task.thread.inuse': '任务线程(用)',
  'task.thread.total': '任务线程(总)'
}
export function metricName (metricKey) {
  return metricMap[metricKey]
}
