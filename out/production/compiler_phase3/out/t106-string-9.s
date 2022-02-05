.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	global_main_s1 : .word 0
	global_main_s2 : .word 0
	userInput_L1:	.space	600
	userInput_L2:	.space	600
	userInput_L4:	.space	600
	userInput_L5:	.space	600

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
		la $a0, global_main_s1
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 8
		la $a0, userInput_L1
		li $a1, 600
		syscall
		move $t0, $a0

		sw $t0, 0($a3)
		la $a0, global_main_s2
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 8
		la $a0, userInput_L2
		li $a1, 600
		syscall
		move $t0, $a0

		sw $t0, 0($a3)
		la $a0, global_main_s1
		lw $t0, 0($a0)
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		la $a0, global_main_s2
		lw $t0, 0($a0)
		addi $sp, $sp, -4
		lw $t1 0($sp)
loop1:
		lb $t3($t1)  
		lb $t4($t0)
		beqz $t3,checkt21 
		beqz $t4,missmatch1
		slt $t5,$t3,$t4  
		bnez $t5,missmatch1
		addi $t1,$t1,1  
		addi $t0,$t0,1
		j loop1
		missmatch1: 
		addi $t1,$zero,1
		j endfunction1
		checkt21:
		bnez $t4,missmatch1
		add $t1,$zero,$zero
		endfunction1:
		move $t0, $t1
		li $v0, 1
		beq $t0, $zero, printFalseL3
		la $t0, true
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		b endPrintFalseL3
	printFalseL3:
		la $t0, false
		li $v0, 4
		add $a0, $t0, $zero
		syscall
	endPrintFalseL3:
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		la $a0, global_main_s1
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 8
		la $a0, userInput_L4
		li $a1, 600
		syscall
		move $t0, $a0

		sw $t0, 0($a3)
		la $a0, global_main_s2
		lw $t0, 0($a0)
		la $a3, 0($a0) 
		li $v0, 8
		la $a0, userInput_L5
		li $a1, 600
		syscall
		move $t0, $a0

		sw $t0, 0($a3)
		la $a0, global_main_s1
		lw $t0, 0($a0)
		move $t1, $t0
		sw $t1, 0($sp)
		addi $sp, $sp, 4
		la $a0, global_main_s2
		lw $t0, 0($a0)
		addi $sp, $sp, -4
		lw $t1 0($sp)
loop2:
		lb $t3($t1)  
		lb $t4($t0)
		beqz $t3,checkt22 
		beqz $t4,missmatch2
		slt $t5,$t3,$t4  
		bnez $t5,missmatch2
		addi $t1,$t1,1  
		addi $t0,$t0,1
		j loop2
		missmatch2: 
		addi $t1,$zero,1
		j endfunction2
		checkt22:
		bnez $t4,missmatch2
		add $t1,$zero,$zero
		endfunction2:
		move $t0, $t1
		li $v0, 1
		beq $t0, $zero, printFalseL6
		la $t0, true
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		b endPrintFalseL6
	printFalseL6:
		la $t0, false
		li $v0, 4
		add $a0, $t0, $zero
		syscall
	endPrintFalseL6:
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
