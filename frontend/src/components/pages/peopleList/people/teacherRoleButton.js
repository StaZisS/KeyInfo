import('../../../../styles/peopleItem.css')

export const ButtonTeacher = ({roles,callback}) => {
    return(
        <button onClick={callback} className={'btn bg-info'}>
            <span className='buttonName'>Преподаватель</span>
        </button>
    )
}
