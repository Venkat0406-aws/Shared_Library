
def call(String stepName, Closure body) {
    def start = System.currentTimeMillis()
    def startTime = new Date()
    echo "${stepName} started at ${startTime.format('yyyy-MM-dd HH:mm:ss')}"

    body()

    def end = System.currentTimeMillis()
    def duration = (end - start) / 1000
    echo "${stepName} finished at ${new Date().format('yyyy-MM-dd HH:mm:ss')}, Duration: ${duration} sec"
}

