case  $1 in
  fibers)
    echo "GET http://localhost:8080/loom/demo" | vegeta attack -name=l-threads -duration=10s -timeout=60s -rate=300 | tee "$1".bin | vegeta report && cat "$1".bin | vegeta plot > "$1".html
      ;;
  threads)
    echo "GET http://localhost:8081/loom/demo" | vegeta attack -name=h-threads -duration=10s -timeout=60s -rate=10 | tee "$1".bin | vegeta report && cat "$1".bin | vegeta plot > "$1".html
      ;;
  *)
    echo "Unknown command. Usage: $0 [fibers|threads]"
esac

# vegeta plot threads.bin fibers.bin > threads-fibers.html