.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_a : .float 0.0
	global_main_temp0: .float 5.50000000
	global_main_temp1: .float 4.69999981

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	runtime_error:
		li $v0, 4
		la $a0, error_run_time
		syscall
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $a0, global_main_a
		l.s $f0, 0($a0)
		la $a3, 0($a0) 
		la $a0, global_main_temp0
		l.s $f0, 0($a0)
		s.s $f0, 0($a3)
		la $a0, global_main_a
		l.s $f0, 0($a0)
		la $a3, 0($a0) 
		la $a0, global_main_a
		l.s $f0, 0($a0)
		mov.s $f1, $f0
		s.s $f1, 0($sp)
		addi $sp, $sp, 4
		la $a0, global_main_temp1
		l.s $f0, 0($a0)
		addi $sp, $sp, -4
		l.s $f1 0($sp)
		add.s $f1, $f1, $f0
		mov.s $f0, $f1
		s.s $f0, 0($a3)
		la $a0, global_main_a
		l.s $f0, 0($a0)
		li $v0, 2
		mov.d	$f12, $f0
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
